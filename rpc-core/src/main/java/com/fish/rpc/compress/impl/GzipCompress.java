package com.fish.rpc.compress.impl;

import cn.hutool.core.util.ZipUtil;
import com.fish.rpc.compress.Compress;

import java.util.Objects;

/**
 * @author Afish
 * @date 2025/4/22 17:27
 */
public class GzipCompress implements Compress {
    @Override
    public byte[] compress(byte[] data) {
        if (Objects.isNull(data) || data.length == 0) {
            return data;
        }
        return ZipUtil.gzip(data);
    }

    @Override
    public byte[] decompress(byte[] data) {
        if (Objects.isNull(data) || data.length == 0) {
            return data;
        }
        return ZipUtil.unGzip(data);
    }
}
