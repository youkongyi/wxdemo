package com.gee.blog.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gee.blog.entity.TextMessage;
import com.gee.blog.utils.MapConverter;
import com.gee.blog.utils.SignUtil;
import com.gee.blog.utils.WechatMessageConstants;
import com.gee.blog.utils.XmlParseMapUtils;

@Controller
public class WxController {
    //访问日志
	private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	/**
	 * 微信服务器认证 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/wx",method=RequestMethod.GET)
	public String register() {
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");
		try {
			// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
			if (SignUtil.checkSignature(signature, timestamp, nonce)) {
				return echostr;
			}
		} catch (Exception ex) {
			return "Illegal Call";
		}
		return "ERROR";
	}

	/**
	 * 接收用户发送的微信消息
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/wx",method = RequestMethod.POST,produces={"text/html;charset=UTF-8;","application/xml;"})
	@SuppressWarnings("rawtypes")
	public String post() {
		try {
		    //xml转换Map(HashMap)不可重复集合
			Map map = XmlParseMapUtils.xmlToMap(request.getInputStream(),new HashMap<String,String>());
			TextMessage message = (TextMessage) MapConverter
			        .packageEntityMapConverter(map, TextMessage.class);
			if(WechatMessageConstants.MESSAGE_TEXT.equals(message.getMsgType())) {
			    TextMessage textMessage = new TextMessage();
	            textMessage.setMsgType(WechatMessageConstants.MESSAGE_TEXT);
	            textMessage.setToUserName(message.getFromUserName());
	            textMessage.setFromUserName(message.getToUserName());
	            textMessage.setCreateTime(System.currentTimeMillis()+"");
	            textMessage.setContent("我已经收到到你发来的消息了");
	            String responseMessage = XmlParseMapUtils.textMessageToXml(textMessage);
	            logger.info(responseMessage);
//	            System.out.println(responseMessage);
	            return responseMessage;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 业务逻辑
		return "error";
	}
}
