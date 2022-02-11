package com.cvc.financial.api.v1.mapper;
import org.springframework.test.util.ReflectionTestUtils;

public class MapperInstancesFactory {

    private MapperInstancesFactory(){}

    public static TransferMapper transferMapper() {
        var transferMapper = TransferMapper.INSTANCE;
        ReflectionTestUtils.setField(transferMapper, "accountMapper", accountMapper());
        return transferMapper;
    }

    public static AccountMapper accountMapper() {
        return AccountMapper.INSTANCE;
    }
}