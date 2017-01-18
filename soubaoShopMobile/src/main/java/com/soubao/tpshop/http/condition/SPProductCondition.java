/**
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2099 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: 飞龙  wangqh01292@163.com
 * Date: @date 2015年11月5日 下午9:23:13 
 * Description: 查询商品相关需要的条件
 * @version V1.0
 */
package com.soubao.tpshop.http.condition;

/**
 * @author 飞龙
 *
 */
public class SPProductCondition extends SPCondition {

	public int categoryID = -1 ;	//分类ID
	public String goodsName;	//分类ID
	public String orderdesc;	//排序方式: desc , asc
	public String orderby;		//排序字段
	public int goodsID;			//商品ID
	public String href;			//请求URL
}
