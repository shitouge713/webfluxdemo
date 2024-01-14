package edu.xinan.demo.service;

import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {
    @Override
    public String sing() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName()+"----,PersonServiceImpl,有人在唱歌");
        return "有人在唱歌";
    }
}
