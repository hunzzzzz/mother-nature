package com.nature.mother.authservice.global.exception

import com.nature.mother.common.exception.ErrorCode

class LoginException(errorCode: ErrorCode) : RuntimeException("[${errorCode}] ${errorCode.message}")