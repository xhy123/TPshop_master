/**
 * tpshop
 * ============================================================================
 * * 版权所有 2015-2027 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ----------------------------------------------------------------------------
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * $Author: Ben  16/07/11 $
 * $description: 商城 -> 我的 -> 头像 -> 个人资料
 */

package com.soubao.tpshop.activity.person.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.soubao.tpshop.R;
import com.soubao.tpshop.activity.common.SPBaseActivity;
import com.soubao.tpshop.activity.common.SPPopuListViewActivity;
import com.soubao.tpshop.activity.common.SPTextFieldViewActivity_;
import com.soubao.tpshop.common.SPMobileConstants;
import com.soubao.tpshop.global.SPMobileApplication;
import com.soubao.tpshop.http.base.SPFailuredListener;
import com.soubao.tpshop.http.base.SPMobileHttptRequest;
import com.soubao.tpshop.http.base.SPSuccessListener;
import com.soubao.tpshop.http.person.SPUserRequest;
import com.soubao.tpshop.model.person.SPUser;
import com.soubao.tpshop.view.SPMoreImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

@EActivity(R.layout.activity_spuser_details)
public class SPUserDetailsActivity extends SPBaseActivity implements View.OnClickListener {

    @ViewById(R.id.phone_num_txt)
    TextView phoneNum;
    @ViewById(R.id.nickname_txtv)
    TextView nickName;
    @ViewById(R.id.sex_txtv)
    TextView sexTxt;
    @ViewById(R.id.age_txtv)
    TextView ageTxt;
    @ViewById(R.id.head_mimgv)
    SPMoreImageView headImage;
    @ViewById(R.id.btn_save)
    Button btnSave;

    SPUser mUser = null;
    private String[] sexA = null;
    private String strSex;
    long birthday = 0;
    Calendar calendar;
    Bitmap mBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCustomerTitle(true, true, getString(R.string.person_user_info));
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void init() {
        super.init();
    }

    @Override
    public void initSubViews() {

    }

    @Override
    public void initData() {
        sexA = getResources().getStringArray(R.array.user_sex_name);
        mUser = SPMobileApplication.getInstance().getLoginUser();
        if (mUser != null) {
            phoneNum.setText(mUser.getMobile());
            try {
                birthday = Long.parseLong(mUser.getBirthday() == null ? "0" : mUser.getBirthday());
                calendar = Calendar.getInstance();
                if (birthday != 0) {
                    calendar.setTimeInMillis(birthday);
                }
                ageTxt.setText(getString(R.string.user_age_format, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DAY_OF_MONTH)));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            strSex = mUser.getSex();
            sexTxt.setText(sexA[stringToInt(mUser.getSex(), 0)]);
            nickName.setText(mUser.getNickname());
            path=Environment.getExternalStorageDirectory().getPath();
            //showToast(path);
            mBitmap = BitmapFactory.decodeFile(path + "/head.jpg");// 从sdcard中获取本地图片,通过BitmapFactory解码,转成bitmap
            if (mBitmap != null) {
                @SuppressWarnings("deprecation")
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), mBitmap);
                circularBitmapDrawable.setCornerRadius(getResources().getDimension(R.dimen.head_corner_35));
                headImage.setImageDrawable(circularBitmapDrawable);

            } else {
                /** 从服务器取,同时保存在本地 ,后续的工作 */
            }
        }
    }

    private int stringToInt(String str, int defaultValue) {
        int res = defaultValue;
        try {
            res = Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public void initEvent() {
        ageTxt.setOnClickListener(this);
        sexTxt.setOnClickListener(this);
        nickName.setOnClickListener(this);
        headImage.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sex_txtv:
                Intent ageIntent = new Intent(this, SPPopuListViewActivity.class);
                ageIntent.putExtra("data", sexA);
                ageIntent.putExtra("defaultIndex", stringToInt(mUser.getSex(), 0));
                startActivityForResult(ageIntent, 102);
                break;
            case R.id.nickname_txtv:
                Intent invokeIntent = new Intent(this, SPTextFieldViewActivity_.class);
                invokeIntent.putExtra("value", mUser.getNickname());
                startActivityForResult(invokeIntent, 101);
                break;
            case R.id.age_txtv:
                showDateDialog();
                break;
            case R.id.head_mimgv:
                selectImage();
                //Crop.pickImage(this);
                break;
            case R.id.btn_save:
                updateUserInfo();
                break;
        }
    }
    private static final int REQUEST_CODE_PHOTO = 0x1;
    private static final int REQUEST_CODE_CAMERA = 0x2;
    private void selectImage() {
        final String[] items = getResources().getStringArray(R.array.user_head_name);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.user_head_title));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //boolean result = Utility.checkPermission(MainActivity.this);
                if (item == 0) {
                    /*
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
                    // ******** code for crop image
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 0);
                    intent.putExtra("aspectY", 0);
                    intent.putExtra("outputX", 200);
                    intent.putExtra("outputY", 150);
                    try {
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, 103);
                    } catch (ActivityNotFoundException e) {
                        // Do nothing for now
                    }*/
                    Intent intent_pat = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent_pat.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                            .fromFile(new File(Environment
                                    .getExternalStorageDirectory(), "head.jpg")));
                    startActivityForResult(intent_pat, REQUEST_CODE_CAMERA);
                } else if (item == 1) {
                    /*
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 0);
                    intent.putExtra("aspectY", 0);
                    intent.putExtra("outputX", 200);
                    intent.putExtra("outputY", 150);
                    try{
                        intent.putExtra("return-data", true);
                        startActivityForResult(Intent.createChooser(intent, "Select File"),104);
                    }catch (Exception e){
                        e.printStackTrace();
                    }*/
                    Intent intent_photo = new Intent(Intent.ACTION_PICK, null);
                    intent_photo
                            .setDataAndType(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    "image/*");
                    startActivityForResult(intent_photo, REQUEST_CODE_PHOTO);
                }
            }
        });
        builder.show();
    }

    private void showDateDialog() {
        if (calendar == null) {
            calendar = Calendar.getInstance();
            if (birthday != 0) {
                calendar.setTimeInMillis(birthday);
            }
        }
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(year, month, day);
                ageTxt.setText(getString(R.string.user_age_format, year, month + 1, day));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 101:
                if (resultCode == SPMobileConstants.Result_Code_GetValue) {
                    nickName.setText(data.getStringExtra("value"));
                }
                break;
            case 102:
                if (resultCode == RESULT_OK) {
                    int index = data.getIntExtra("index", 0);
                    strSex = "" + index;
                    sexTxt.setText(sexA[index]);
                }
                break;
            case 103:
                onCaptureImageResult(data);
                break;
            case 104:
                onSelectFromGalleryResult(data);
                break;
            case REQUEST_CODE_PHOTO:
                if (resultCode == RESULT_OK) {
                    shearPhoto(data.getData());// 剪切图片
                }
                break;
            case REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    File fileTemp = new File(
                            Environment.getExternalStorageDirectory() + "/head.jpg");
                    shearPhoto(Uri.fromFile(fileTemp));// 剪切图片
                }
                break;
            case 3:
                if (data != null) {
                    Bundle mBundle = data.getExtras();
                    mBitmap = mBundle.getParcelable("data");
                    if (mBitmap != null) {
                        /** 上传到服务器 待--- */
                        setPictureToSD(mBitmap);// 保存本地
                        headImage.setImageBitmap(mBitmap);// 显示
                    }
                }
                break;
            }

    }
    private String path =  "/sdcard/headPhoto";// 存放本地的头像照片
    private void setPictureToSD(Bitmap bitmap) {

        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {// 检测SD卡的可用性
            return;
        }
        FileOutputStream fos = null;

        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "/head.jpg";// 图片名字
        try {
            fos = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 压缩后写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭输出流
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @Title: shearPhoto
     * @Description:直接调用系统的剪切功能
     * @param: @param uri
     * @return: void
     * @throws
     */
    private void shearPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void updateUserInfo() {
        String nickNameStr = nickName.getText().toString();
        String sex = strSex;//sexTxt.getText().toString();
        String age = calendar.getTimeInMillis() + ""; //ageTxt.getText().toString();
        SPUser user = mUser;
        user.setNickname(nickNameStr);
        user.setSex(sex);
        user.setBirthday(age);
        SPUserRequest.updateUserInfo(user, new SPSuccessListener() {
            @Override
            public void onRespone(String msg, Object response) {
                showToast(msg);
                finish();
            }
        }, new SPFailuredListener(SPUserDetailsActivity.this) {
            @Override
            public void onRespone(String msg, int errorCode) {
                showToast(msg);
                finish();
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            headImage.setImageBitmap(photo);
        }
    }
    private void onCaptureImageResult(Intent data) {
        Bundle extras = data.getExtras();
        if (extras == null) {
            return;
        }
        Bitmap thumbnail = data.getExtras().getParcelable("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        headImage.setImageBitmap(thumbnail);
    }

}
