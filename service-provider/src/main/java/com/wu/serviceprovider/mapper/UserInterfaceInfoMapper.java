package com.wu.serviceprovider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wu.commonapi.model.entity.UserInterfaceInfo;

import java.util.List;

/**
 * @Entity com.wu.serviceprovider.model.entity.UserInterfaceInfo
 */
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




