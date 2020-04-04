package org.smart4j.chapter1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter1.model.Customer;
import org.smart4j.chapter1.util.DataBaseHelper;
import org.smart4j.chapter1.util.PropsUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CustomerService {

    public static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class );


    public List<Customer> getCustomerList(){
        //TODO

        try{
            List<Customer> customerList = new ArrayList<Customer>();
            String sql =" select * from customer";
//            conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
          //  conn = DataBaseHelper.getConnection();
           /* PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while ( rs.next()){
                Customer customer = new Customer();
                customer.setId(rs.getLong("id"));
                customer.setName(rs.getString("name"));
                customer.setContract(rs.getString("contact"));
                customer.setTelephone(rs.getString("telephone"));
                customer.setEmail(rs.getString("email"));
                customer.setRemark(rs.getString("remark"));
                customerList.add(customer);
            }*/
           customerList = DataBaseHelper.queryEntityList(Customer.class,sql,null);
            return customerList;
        }/*catch(SQLException e){
            LOGGER.error("execete sql failure",e);

        }*/finally {
            DataBaseHelper.closeConnection();
        }

    }
    public Customer getCusomter(long id){
        //TODO
        return null;
    }

    public boolean createCustomer(Map<String,Object> filedMap ){
     return DataBaseHelper.insertEntity(Customer.class,filedMap);
    }
    public boolean updateCustomer(long id,Map<String,Object> fieldMap){

        return DataBaseHelper.updateEntity(Customer.class,id,fieldMap);
    }
    public boolean deleteCustomer(long id){
        return DataBaseHelper.deleteEntity(Customer.class,id);
    }
}
