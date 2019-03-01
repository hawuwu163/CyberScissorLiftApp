package com.cyber.ScissorLiftApp.module.protocol;

import java.util.List;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp
 * @date 2019/1/19 15:07
 * @Description: 基础参数列表解析接口,
 */
public interface IProtocol {
    List<Byte> parsingToCipher(List<Integer> list);

    List<Integer> parsingToPlain(List<Byte> list);

    byte[] getRequireCode();
}
