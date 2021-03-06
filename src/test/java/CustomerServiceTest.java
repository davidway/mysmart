import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.smart4j.chapter1.model.Customer;
import org.smart4j.chapter1.service.CustomerService;
import org.smart4j.chapter1.util.DataBaseHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerServiceTest {
    private final CustomerService customerService;
    public CustomerServiceTest(){
        customerService = new CustomerService();
    }

    @Before
    public void init() throws IOException {
        DataBaseHelper.executeSqlFile("sql/update.sql");
    }




    @Test
    public void getCustomerListTest() throws Exception{
        List<Customer> customerList = customerService.getCustomerList();
        Assert.assertEquals(2,customerList.size());
    }
    @Test
    public void getCustomerTest(){
        long id =1;
        Customer customer = customerService.getCusomter(id);
        Assert.assertNotNull(customer);
    }
    @Test
    public void createCustomerTest(){
        Map<String,Object> fieldMap = new HashMap<String,Object>();
        fieldMap.put("name","customer100");
        fieldMap.put("contact","John");
        fieldMap.put("telephone","13512345678");
        boolean result = customerService.createCustomer(fieldMap);
        Assert.assertTrue(result);
    }

    @Test
    public void updateCustomerTest(){
        long id = 1;
        Map<String,Object> fieldMap = new HashMap<String,Object>();
        fieldMap.put("contact","eric");
        boolean result = customerService.updateCustomer(id,fieldMap);
        Assert.assertTrue(result);
    }


    @Test
    public void deleteCustomerTest(){
        long id = 1;

        boolean result = customerService.deleteCustomer(id);
        Assert.assertTrue(result);
    }

}
