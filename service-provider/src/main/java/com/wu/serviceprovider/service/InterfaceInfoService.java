package com.wu.serviceprovider.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.commonapi.model.entity.InterfaceInfo;
import com.wu.serviceprovider.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    /**
     * 测试接口
     * @param interfaceInfoInvokeRequest
     * @param request
     * @return
     */
    String testInterface(InterfaceInfoInvokeRequest interfaceInfoInvokeRequest,
                         HttpServletRequest request);
}
