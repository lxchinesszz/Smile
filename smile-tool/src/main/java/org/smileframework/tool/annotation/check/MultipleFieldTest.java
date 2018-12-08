package org.smileframework.tool.annotation.check;

import lombok.Data;
import net.sf.oval.constraint.Assert;

/**
 * @author liuxin
 * @version Id: Person.java, v 0.1 2018/10/22 11:06 AM
 */
@Data
public class MultipleFieldTest {

    @MultipleField(msg = "name和status不能同时为空", values = {"status"})
    private String name;

    @Assert(expr = "_value == _this.name", lang = "groovy", message = "userName必须与name一致", when = "groovy:_this.status != null")
    private String userName;

    @MultipleField(msg = "status和userName不能同时为空", values = "userName")
    private String status;


    public static void main(String[] args) {
        MultipleFieldTest person = new MultipleFieldTest();
        person.setStatus("1");
        person.setName("zhanglei");
        person.setUserName("zhanglei");
        CustomerAnnotationValidator validator = new CustomerAnnotationValidator();
        validator.validate(person);
        validator.validate(person);
    }

}
