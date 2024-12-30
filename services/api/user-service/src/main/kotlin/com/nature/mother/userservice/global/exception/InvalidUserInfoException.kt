package com.nature.mother.userservice.global.exception

import com.nature.mother.common.exception.ErrorCode

class InvalidUserInfoException(errorCode: ErrorCode) : RuntimeException("[${errorCode}] ${errorCode.message}")