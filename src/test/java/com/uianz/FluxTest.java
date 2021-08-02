package com.uianz;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.Disposable;
import reactor.core.Exceptions;
import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author uianz
 * @date 2021/7/27
 * 参考 https://blog.csdn.net/get_set/article/details/79480172
 */
public class FluxTest {

    private Flux<String> getZipDescFlux() {
        String desc = "Zip two sources together, that is to say wait for all the sources to emit one element and " +
                "combine these elements once into a Tuple2.";
        return Flux.fromArray(desc.split("\\s+"));  // 1
    }

    @Test
    public void testSimpleOperators() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);  // 2
        Flux.zip(
                getZipDescFlux(),
                Flux.interval(Duration.ofMillis(200))
        )  // 3
                .subscribe(t -> System.out.println(t.getT1()), null, countDownLatch::countDown);    // 4
        countDownLatch.await(10, TimeUnit.SECONDS);     // 5
    }

    private String getStringSync() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello, Reactor!";
    }

    @Test
    public void testSyncToAsync() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Mono.fromCallable(this::getStringSync)    // 1
                .subscribeOn(Schedulers.boundedElastic())  // 2
                .subscribe(System.out::println, null, countDownLatch::countDown);
        countDownLatch.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void testError() {
        Flux.range(1, 6)
                .map(i -> 10 / (i - 3))
                .onErrorReturn(0) //异常时返回0并终止流
                .map(i -> i * i)
                .subscribe(System.out::println, System.err::println);
        System.out.println("===========");
        Flux.range(1, 6)
                .map(i -> 10 / (i - 3))
                .onErrorContinue((var e, var i) -> System.out.println("error num is " + i))  //异常时跳过
                .onErrorResume(e -> {
                    System.out.println(e);
                    return Mono.just(new Random().nextInt(6));
                }) // 提供新的数据流，当执行onErrorContinue时会跳过此方法
                .map(i -> i * i)
                .subscribe(System.out::println, System.err::println);

        System.out.println("===========");
        Flux.just("timeout1")
                .map(k -> 1 / 0)
                .onErrorMap(original -> new RuntimeException("SLA exceeded", original))
                .subscribe(System.out::println, System.err::println);
    }

    @Test
    public void testFinally() {
        var statsCancel = new LongAdder();
        var flux = Flux.just("foo", "bar")
                .doFinally(type -> {
                    if (type == SignalType.CANCEL) {
                        statsCancel.increment(); //自增
                    }
                }).take(1);
        flux.subscribe(System.out::println);
    }

    @Test
    public void testTry() {
//        Flux.range(1, 6)
//                .map(i -> 10 / (3 - i))
//                .retry(2)
//                .subscribe(System.out::println, System.err::println);
//        ThreadUtil.sleep(1000);

        //retryBackoff方法不知道为什么被弃用，替代方法为retryWhen
        //https://github.com/oddy-bassey/learn-reactive-spring/issues/12

        Retry retrySpec = Retry.fixedDelay(2, Duration.ofMillis(1000))
                .filter((ex) -> ex instanceof RuntimeException)
                .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> Exceptions.propagate(retrySignal.failure())));
        Flux<String> stringFlux;
        stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .concatWith(Flux.just("D"))
                .onErrorMap(ex -> new RuntimeException(ex.getMessage()))
                .retryWhen(retrySpec)
                .log();

        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void testBackpressure() {
//        Flux.range(1, 6)    // 1
//                .doOnRequest(n -> System.out.println("Request " + n + " values..."))    // 2
//                .subscribe(new BaseSubscriber<>() {  // 3
//                    @Override
//                    protected void hookOnSubscribe(Subscription subscription) { // 4
//                        System.out.println("Subscribed and make a request...");
//                        request(1); // 5
//                    }
//
//                    @Override
//                    protected void hookOnNext(Integer value) {  // 6
//                        try {
//                            TimeUnit.SECONDS.sleep(1);  // 7
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        System.out.println("Get value [" + value + "]");    // 8
//                        request(1); // 9
//                    }
//                });
        Flux.range(1, 6)
                .doOnRequest(n -> System.out.println("Request " + n + " values..."))
                .limitRate(3)
                .subscribe(System.out::println);
//        Flux.range(1, 10)
//                .log()
//                .subscribe(new BaseSubscriber<Integer>() {
//                    @Override
//                    protected void hookOnSubscribe(Subscription subscription) {
////                        request(1);
//                        request(2);
//                    }
//                });
//        ThreadUtil.sleep(2000);
    }

    @Test
    public void testGenerate1() {
        final AtomicInteger count = new AtomicInteger(1);   // 1
        Flux.generate(sink -> {
            sink.next(count.get() + " : " + new Date());   // 2
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (count.getAndIncrement() >= 5) {
                sink.complete();     // 3
            }
        }).subscribe(System.out::println);  // 4
    }

    @Test
    public void testScheduling() {
        Flux.range(0, 10)
                .log()    // 1
                .publishOn(Schedulers.newParallel("myParallel"))
                .log()    // 2
                .subscribeOn(Schedulers.newElastic("myElastic"))
                .log()    // 3
                .blockLast();
    }


    @Test
    public void testBackoff() {
        //before
//        Flux<String> flux =
//                Flux.<String>error(new IllegalArgumentException())
//                        .retryWhen(companion -> companion
//                                .doOnNext(s -> System.out.println(s + " at " + LocalTime.now()))
//                                .zipWith(Flux.range(1, 4), (error, index) -> {
//                                    if (index < 4) return index;
//                                    else throw Exceptions.propagate(error);
//                                })
//                                .flatMap(index -> Mono.delay(Duration.ofMillis(index * 100)))
//                                .doOnNext(s -> System.out.println("retried at " + LocalTime.now()))
//                        );


//        AtomicInteger errorCount = new AtomicInteger();
        Flux.<String>error(new IllegalStateException("boom"))
                .doOnError(e -> {
//                            errorCount.incrementAndGet();
                    System.out.println(e + " at " + LocalTime.now());
                })
                .retryWhen(Retry
                        .backoff(3, Duration.ofMillis(1000)).jitter(0d)
                        .doAfterRetry(rs -> System.out.println("retried at " + LocalTime.now()))
                        .onRetryExhaustedThrow((spec, rs) -> rs.failure())
                ).blockLast();
    }

    @Test
    public void testConnectableFlux1() throws InterruptedException {
        Flux<Integer> source = Flux.range(1, 3)
                .doOnSubscribe(s -> System.out.println("上游收到订阅"));

        ConnectableFlux<Integer> co = source.publish();

        co.subscribe(System.out::println, e -> {}, () -> {});
        co.subscribe(System.out::println, e -> {}, () -> {});

        System.out.println("订阅者完成订阅操作");
        Thread.sleep(500);
        System.out.println("还没有连接上");

        co.connect();
    }

    @Test
    public void testConnectableFluxAutoConnect() throws InterruptedException {
        Flux<Integer> source = Flux.range(1, 3)
                .doOnSubscribe(s -> System.out.println("上游收到订阅"));

        // 需要两个订阅者才自动连接
        Flux<Integer> autoCo = source.publish().autoConnect(2);

        autoCo.subscribe(System.out::println, e -> {}, () -> {});
        System.out.println("第一个订阅者完成订阅操作");
        Thread.sleep(500);
        System.out.println("第二个订阅者完成订阅操作");
        autoCo.subscribe(System.out::println, e -> {}, () -> {});
        Thread.sleep(500);
        //因为已经满足两个订阅条件，所以消息开始消费了，第三个订阅着开始订阅时队列已经没有消息可以消费了
        System.out.println("第三个订阅者完成订阅操作");
        autoCo.subscribe(System.out::println, e -> {}, () -> {});
    }

    @Test
    public void testConnectableFluxRefConnect() throws InterruptedException {

        Flux<Long> refCounted = Flux.interval(Duration.ofMillis(500))
                .doOnSubscribe(s -> System.out.println("上游收到订阅"))
                .doOnCancel(() -> System.out.println("上游发布者断开连接"))
                .publish()
                //最少两名订阅着才开始发出数据,当所有订阅着取消订阅时，如果不能在两秒内及时接入，则会断开连接
                .refCount(2, Duration.ofSeconds(2))
                ;

        System.out.println("第一个订阅者订阅");
        Disposable sub1 = refCounted.subscribe(l -> System.out.println("sub1: " + l));

        TimeUnit.SECONDS.sleep(1);
        System.out.println("第二个订阅者订阅");
        Disposable sub2 = refCounted.subscribe(l -> System.out.println("sub2: " + l));

        TimeUnit.SECONDS.sleep(1);
        System.out.println("第一个订阅者取消订阅");
        sub1.dispose();

        TimeUnit.SECONDS.sleep(1);
        System.out.println("第二个订阅者取消订阅");
        sub2.dispose();

        TimeUnit.SECONDS.sleep(1);
        System.out.println("第三个订阅者订阅");
        Disposable sub3 = refCounted.subscribe(l -> System.out.println("sub3: " + l));

        TimeUnit.SECONDS.sleep(1);
        System.out.println("第三个订阅者取消订阅");
        sub3.dispose();

        TimeUnit.SECONDS.sleep(3);
        System.out.println("第四个订阅者订阅");
        Disposable sub4 = refCounted.subscribe(l -> System.out.println("sub4: " + l));
        TimeUnit.SECONDS.sleep(1);
        System.out.println("第五个订阅者订阅");
        Disposable sub5 = refCounted.subscribe(l -> System.out.println("sub5: " + l));
        TimeUnit.SECONDS.sleep(2);
    }

    public static void main(String[] args) {
//        Flux.generate(() -> 0,
//                (state, sink) -> {
//                    sink.next("3 x " + state + " = " + 3 * state);
//                    if (state == 5) sink.complete();
//                    return state + 1;
//                })´
//                .map(x -> {
//                    System.out.println(x);
//                    return x;
//                }).blockLast(Duration.ofSeconds(10));

//        var words = List.of("th", "qu");
//        var manyLetters = Flux.fromIterable(words)
//                .flatMap(word -> {
//                    System.out.println("Step1=" + word);
//                    return Flux.fromArray(word.split(""));
//                })
//                .filter(s -> {
//                    System.out.println("Step2=" + s);
//                    return true;
//                })
//                .filter(s -> {
//                    System.out.println("Step3=" + s);
//                    return true;
//                });
//        manyLetters.subscribe(s -> System.out.println("Result=" + s + "\n"));


//        Flux.just(1, 2, 3, 4, 5, 6).subscribe(
//                System.out::println,
//                System.err::println,
//                () -> System.out.println("Completed!"));

//        Mono.error(new Exception("some error")).subscribe(
//                System.out::println,
//                e -> System.out.println("error message= " +  e.toString()),
//                () -> System.out.println("Completed!")
//        );

        StepVerifier.create(
                Flux.just("flux", "mono")
                        .flatMap(s -> Flux.fromArray(s.split("\\s*"))   // 1
                                .delayElements(Duration.ofMillis(100))
                        ) // 2
                        //打印结果为mfolnuox，原因在于各个拆分后的小字符串都是间隔100ms发出的，因此会交叉
                        //时间越短顺序可能都不一样
                        .doOnNext(System.out::print)) // 3
                .expectNextCount(8) // 4
                .verifyComplete();
    }

}
