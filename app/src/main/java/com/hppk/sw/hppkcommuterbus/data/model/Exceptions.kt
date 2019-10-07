package com.hppk.sw.hppkcommuterbus.data.model

import java.lang.Exception

class BusLineNotFoundExceptions(msg: String): Exception(msg)

class BusLineSaveFailedExceptions(msg: String): Exception(msg)