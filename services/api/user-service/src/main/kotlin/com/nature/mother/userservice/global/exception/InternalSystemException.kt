package com.nature.mother.userservice.global.exception

import com.nature.mother.common.exception.ErrorCode

class InternalSystemException(errorCode: ErrorCode) : RuntimeException("[${errorCode}] ${errorCode.message}")