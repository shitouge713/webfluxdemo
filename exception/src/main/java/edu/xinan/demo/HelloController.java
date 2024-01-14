package edu.xinan.demo;

import edu.xinan.demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("hello")
public class HelloController {

    @Autowired
    PersonService personService;

    @GetMapping("error")
    public String sayHello(String name) {
        int i = 1 / 0;
        return "controller: " + i;
    }


    @GetMapping("/user")
    public Mono<Object> getUser() {
        System.out.println(Thread.currentThread().getName() + "---=========here get=====");
        //组装数据序列
        Mono<Object> mono = Mono.create(sink -> {
            sink.success(personService.sing());
        }).doOnSubscribe(sub -> {//订阅数据
            System.out.println(Thread.currentThread().getName() + "---1. doOnSubscribe..." + sub);
        }).doOnNext(data -> {//得到数据
            //System.out.println(Thread.currentThread().getName() + "---2. data:" + data);
        }).doOnSuccess(onSuccess -> {//整体完成
            //System.out.println(Thread.currentThread().getName() + "---3. onSuccess:" + onSuccess);
        });
        //System.out.println(Thread.currentThread().getName() + "---before return, mono: " + mono);

        // 得到一个包装的数据序列，return给了容器
        // 容器拿到这个序列，再去执行序列里的方法
        // 这和 ajax 很像
        // 1. 写回调接口，让b调用
        // 2. 将方法传过去，看起来像是异步，实质上，阻塞过程在容器内部
        // 并不是提高效率，只是将阻塞延后
        //System.out.println("return方法线程名称：" + Thread.currentThread().getName());
        // 组织数据的过程，是netty容器做的，获取数据的过程不依赖controller了
        return mono;
    }

    @GetMapping("hello")
    private String hello(String name) throws InterruptedException {
        Thread.sleep(10000);
        String result = String.format("hello %s, current-thread is [%s]", name, Thread.currentThread().getName());
        System.out.println(result);
        return result;
    }

    @GetMapping("nob")
    public Mono<String> nob() {
        System.out.println(Thread.currentThread().getName());
        return Mono.just(Thread.currentThread().getName());
    }

    @ExceptionHandler(Exception.class)
    public String test(Exception e) {
        return "@ExceptionHandler: " + e.getMessage();
    }
}
