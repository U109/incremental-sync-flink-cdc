package com.zzz.flink.flinkcdcproducer.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author zhangzhongzhen wrote on 2024/3/25
 * @version 1.0
 * @description:
 */
public class TestMain {

    public static void main(String[] args) {

        String triggerSQL = "create or replace trigger tri_sales  \n" +
                " \n" +
                "for update of sales_amt on SALES  \n" +
                " \n" +
                "compound trigger  \n" +
                " \n" +
                "type ty_sales_log is table of sales_log%rowtype  \n" +
                " \n" +
                "index by pls_integer;  \n" +
                " \n" +
                "coll_sales_log ty_sales_log;  \n" +
                " \n" +
                "ctr pls_integer:=0;  \n" +
                " \n" +
                "before statement is  \n" +
                " \n" +
                "begin  \n" +
                " \n" +
                "dbms_output.put_line('in before statement');  \n" +
                " \n" +
                "end before statement;  \n" +
                " \n" +
                "before each row is  \n" +
                " \n" +
                "begin  \n" +
                " \n" +
                "dbms_output.put_line('in before each row');  \n" +
                " \n" +
                "end before each row;  \n" +
                " \n" +
                "after each row is  \n" +
                " \n" +
                "begin  \n" +
                " \n" +
                "ctr := ctr+1;  \n" +
                " \n" +
                "dbms_output.put_line('in after each row.sales_amt'||:new.sales_amt);  \n" +
                " \n" +
                "coll_sales_log(ctr).sales_id := :old.sales_id;  \n" +
                " \n" +
                "coll_sales_log(ctr).cust_id := :old.cust_id;  \n" +
                " \n" +
                "coll_sales_log(ctr).sales_amt := :new.sales_amt;  \n" +
                " \n" +
                "end  after each row;  \n" +
                " \n" +
                "after statement is  \n" +
                " \n" +
                "begin  \n" +
                " \n" +
                "dbms_output.put_line('in after statement');  \n" +
                " \n" +
                "forall counter in 1..coll_sales_log.count()  \n" +
                " \n" +
                "insert into sales_log(sales_id,cust_id,sales_amt)    \n" +
                " \n" +
                "values (coll_sales_log(counter).sales_id,coll_sales_log(counter).cust_id,coll_sales_log(counter).sales_amt);  \n" +
                " \n" +
                "end after statement;             /* 不能使用Insert into sales_log  values    \n" +
                " \n" +
                "(coll_sales_log(counter));添加*/  \n" +
                " \n" +
                "end tri_sales; ";
        String encodeToString = Base64.getEncoder().encodeToString(triggerSQL.getBytes());
        System.out.println(encodeToString);


        byte[] decode = Base64.getDecoder()
                .decode("Y3JlYXRlIG9yIHJlcGxhY2UgdHJpZ2dlciB0cmlfc2FsZXMgIAogCmZvciB1cGRhdGUgb2Ygc2FsZXNfYW10IG9uIFNBTEVTICAKIApjb21wb3VuZCB0cmlnZ2VyICAKIAp0eXBlIHR5X3NhbGVzX2xvZyBpcyB0YWJsZSBvZiBzYWxlc19sb2clcm93dHlwZSAgCiAKaW5kZXggYnkgcGxzX2ludGVnZXI7ICAKIApjb2xsX3NhbGVzX2xvZyB0eV9zYWxlc19sb2c7ICAKIApjdHIgcGxzX2ludGVnZXI6PTA7ICAKIApiZWZvcmUgc3RhdGVtZW50IGlzICAKIApiZWdpbiAgCiAKZGJtc19vdXRwdXQucHV0X2xpbmUoJ2luIGJlZm9yZSBzdGF0ZW1lbnQnKTsgIAogCmVuZCBiZWZvcmUgc3RhdGVtZW50OyAgCiAKYmVmb3JlIGVhY2ggcm93IGlzICAKIApiZWdpbiAgCiAKZGJtc19vdXRwdXQucHV0X2xpbmUoJ2luIGJlZm9yZSBlYWNoIHJvdycpOyAgCiAKZW5kIGJlZm9yZSBlYWNoIHJvdzsgIAogCmFmdGVyIGVhY2ggcm93IGlzICAKIApiZWdpbiAgCiAKY3RyIDo9IGN0cisxOyAgCiAKZGJtc19vdXRwdXQucHV0X2xpbmUoJ2luIGFmdGVyIGVhY2ggcm93LnNhbGVzX2FtdCd8fDpuZXcuc2FsZXNfYW10KTsgIAogCmNvbGxfc2FsZXNfbG9nKGN0cikuc2FsZXNfaWQgOj0gOm9sZC5zYWxlc19pZDsgIAogCmNvbGxfc2FsZXNfbG9nKGN0cikuY3VzdF9pZCA6PSA6b2xkLmN1c3RfaWQ7ICAKIApjb2xsX3NhbGVzX2xvZyhjdHIpLnNhbGVzX2FtdCA6PSA6bmV3LnNhbGVzX2FtdDsgIAogCmVuZCAgYWZ0ZXIgZWFjaCByb3c7ICAKIAphZnRlciBzdGF0ZW1lbnQgaXMgIAogCmJlZ2luICAKIApkYm1zX291dHB1dC5wdXRfbGluZSgnaW4gYWZ0ZXIgc3RhdGVtZW50Jyk7ICAKIApmb3JhbGwgY291bnRlciBpbiAxLi5jb2xsX3NhbGVzX2xvZy5jb3VudCgpICAKIAppbnNlcnQgaW50byBzYWxlc19sb2coc2FsZXNfaWQsY3VzdF9pZCxzYWxlc19hbXQpICAgIAogCnZhbHVlcyAoY29sbF9zYWxlc19sb2coY291bnRlcikuc2FsZXNfaWQsY29sbF9zYWxlc19sb2coY291bnRlcikuY3VzdF9pZCxjb2xsX3NhbGVzX2xvZyhjb3VudGVyKS5zYWxlc19hbXQpOyAgCiAKZW5kIGFmdGVyIHN0YXRlbWVudDsgICAgICAgICAgICAgLyog5LiN6IO95L2/55SoSW5zZXJ0IGludG8gc2FsZXNfbG9nICB2YWx1ZXMgICAgCiAKKGNvbGxfc2FsZXNfbG9nKGNvdW50ZXIpKTvmt7vliqAqLyAgCiAKZW5kIHRyaV9zYWxlczsg");

        System.out.println(new String(decode, StandardCharsets.UTF_8));
    }
}
