package com.levibostian.androidblanky.service.service.endpoint.contract

abstract class Contract {

    abstract fun verify(): ContractNotMet?

    abstract class ContractNotMet

}