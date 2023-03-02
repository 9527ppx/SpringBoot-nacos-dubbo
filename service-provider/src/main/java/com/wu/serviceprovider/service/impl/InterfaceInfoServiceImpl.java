package com.wu.serviceprovider.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.wu.commonapi.model.entity.User;
import com.wu.commonapi.utils.SignUtils;
import com.wu.serviceprovider.common.ErrorCode;
import com.wu.serviceprovider.exception.BusinessException;
import com.wu.serviceprovider.mapper.InterfaceInfoMapper;
import com.wu.serviceprovider.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.wu.serviceprovider.service.InterfaceInfoService;
import com.wu.commonapi.model.entity.InterfaceInfo;
import com.wu.serviceprovider.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {

    @Resource
    private UserService userService;

//    private static final String GATEWAY_HOST = "http://localhost:8090";
    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }

    @Override
    public String testInterface(InterfaceInfoInvokeRequest interfaceInfoInvokeRequest,
                                HttpServletRequest request) {

        //用户请求的参数
        String userRequestParams = interfaceInfoInvokeRequest.getUserRequestParams();
//        userRequestParams = userRequestParams == null ? "" : userRequestParams;
        //拿到用户sk/ak
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();

        InterfaceInfo interfaceInfo = this.getById(interfaceInfoInvokeRequest.getId());

        String url = interfaceInfo.getUrl();
        if (url == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"url不能为空");
        }
        // 数据库里存的默认参数
        String oldRequestParams = interfaceInfo.getRequestParams();
        if (StringUtils.isNotBlank(userRequestParams)){ // 不为空 true   requestParams
            // 不为空改为用户传的参数
            oldRequestParams=userRequestParams;
        }
        String requestHeader = interfaceInfo.getRequestHeader();
        if (requestHeader == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String method = interfaceInfo.getMethod();

        //处理请求头
        Gson gson = new Gson();
        HashMap<String,String> requestHeaderMap = gson.fromJson(requestHeader, HashMap.class);
        //get请求下oldRequestParams传form需要转map
        HashMap<String,Object> requestParamsMap = gson.fromJson(oldRequestParams, HashMap.class);

        requestHeaderMap.put("accessKey", accessKey);
        requestHeaderMap.put("nonce", RandomUtil.randomNumbers(4));
        requestHeaderMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        requestHeaderMap.put("sign", SignUtils.genSign(secretKey) );
        //发送请求
        //GET,
        //    POST,
        //    HEAD,
        //    OPTIONS,
        //    PUT,
        //    DELETE,
        //    TRACE,
        //    CONNECT,
        //    PATCH;
        HttpRequest requestMethod =null;
        switch (method)
        {
            case "get":
                requestMethod = HttpUtil.createRequest(Method.GET, url).form(requestParamsMap);
                break;
            case "post":
                requestMethod = HttpUtil.createRequest(Method.POST, url).body(oldRequestParams);
                break;
            case "head":
                requestMethod = HttpUtil.createRequest(Method.HEAD, url);
                break;
            case "put":
                requestMethod = HttpUtil.createRequest(Method.PUT, url);
                break;
            case "delete":
                requestMethod = HttpUtil.createRequest(Method.DELETE, url);
                break;
            default:
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求方法错误");
        }


//        hashMap.put("accessKey", accessKey);
        // 一定不能直接发送
//        hashMap.put("secretKey", secretKey);
//        hashMap.put("nonce", RandomUtil.randomNumbers(4));
//        hashMap.put("body", userRequestParams);
//        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        // 根据请求的值进行加密
//        hashMap.put("sign", SignUtils.genSign(userRequestParams, secretKey));





        HttpResponse httpResponse = requestMethod
                .addHeaders(requestHeaderMap)
                .execute();

        System.out.println(httpResponse.getStatus());
        String result = httpResponse.body();
        System.out.println(result);

        return result;
    }


}




