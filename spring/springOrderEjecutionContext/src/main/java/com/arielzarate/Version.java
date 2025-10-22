package com.arielzarate;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Version {

    private final int major;
    private final int minor;
    private final int revision;

    //se inyecta en el constructor automaticamente porque uso @AllArgsConstructor
    //y es un componente de spring porque uso @Component


    @Override
    public String toString() {
        return major + "." + minor + "." + revision;
    }


}
