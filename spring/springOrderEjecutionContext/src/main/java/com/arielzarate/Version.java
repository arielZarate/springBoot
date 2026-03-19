package com.fake_store_login;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class Version {

    private int major;
    private int minor;
    private int revision;

    //se inyecta en el constructor automaticamente porque uso @AllArgsConstructor
    //y es un componente de spring porque uso @Component


    @Override
    public String toString() {
        return major + "." + minor + "." + revision;
    }


}
