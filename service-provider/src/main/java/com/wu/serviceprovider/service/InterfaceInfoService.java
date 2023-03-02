package com.wu.serviceprovider.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wu.commonapi.model.entity.InterfaceInfo;

/**
 *
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
