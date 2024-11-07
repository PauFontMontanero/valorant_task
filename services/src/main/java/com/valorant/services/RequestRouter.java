package com.valorant.services;

import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

public interface RequestRouter {
    RawHttpResponse<?> execRequest(RawHttpRequest request);
}