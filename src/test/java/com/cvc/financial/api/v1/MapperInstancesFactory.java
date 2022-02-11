package com.cvc.financial.api.v1;
import com.cvc.financial.api.v1.mapper.AccountMapper;
import com.cvc.financial.api.v1.mapper.TransferMapper;
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