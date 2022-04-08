package org.test;

import com.sihuan.prototype.ConfigAnnotation;
import com.sihuan.prototype.ConfigClientConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

/**
 * 真实情况fields不会都是String，出于演示目的，这里简化了复杂类型转换的场景
 */
@Component
@ConfigAnnotation(value = "testConfig", format = ConfigAnnotation.Format.PROPERTIES)
public class ConfigBean {
    private String name;
    private String job;
    private String company;
    private String salary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "ConfigBean{" +
                "name='" + name + '\'' +
                ", job='" + job + '\'' +
                ", company='" + company + '\'' +
                ", salary='" + salary + '\'' +
                '}';
    }

    @Configuration
    @Import(ConfigClientConfiguration.class)
    @ComponentScan(value = "org.test")
    public static class CustomerConfiguration {
    }
}

