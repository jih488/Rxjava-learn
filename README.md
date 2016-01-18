# Rxjava-learn

1、github 项目地址：  https://github.com/ReactiveX/RxJava
（RxAndroid 是一个专门为Android开发做出最小适配框架，可以方便的写出响应式的组件和一站式服务）

2、rxjava是一个响应式的编程框架，基于观察者模式。
其中关键的要素就是Observable(事件产生者,也是事件的观察者)  Operator（操作符） Subscriber（事件的消费者，也是事件的订阅者）

特点：使用可观察序列编写异步和事件驱动的库，扩展了观察者模式以支持数据和事件序列    ，并且加入了operator操作符来实现数据转换和事件过滤
a、易于并发从而更好的利用服务器的能力      （可以自由指定运行的线程）
b、易于有条件的异步执行                               （通过操作符控制流程）
c、一种更好的方式来避免回调地狱                 （多个事件源基于组合而不是嵌套）
d、一种响应式方法                                          （编码更简单）

优点：将开发者的注意力从低级别的线程同步、线程安全、并发数据对象这些问题中转移

3、一个简单的示例

A、创建一个Observable
Observable<String> myObservable = Observable.create(
    new Observable.OnSubscribe<String>() {
         @Override
public void call(Subscriber<? super String>sub) {
           sub.onNext("Hello, world!");
           sub.onCompleted();
         }
    });

B、创建一个Subscriber
Subscriber<String> mySubscriber = new Subscriber<String>() {
     @Override
     public void onNext(String s) {
          System.out.println(s);

     }
     @Override
     public void onCompleted() {
     }
     @Override
     public void onError(Throwable e) {
     }
};

C、将两者联系起来
myObservable.subscribe(mySubscriber); // Outputs "Hello, world!"

这是一个展示流程的例子：myObservable是事件源，mySubscriber是订阅者，通过Observable的subscribe方法，将事件输出给订阅者去消费。

4、什么是Observable   Observer   Subscriber   Subscription
   Observable  事件观察者或者事件生产者  这两种叫法是针对不同的对象而言的。第一：对于Subscriber它是事件的生产者，因为当使用subscribe方法对一个Observable添加一个订阅者的时候，这个时候会立即调用Observable的call方法，将产生的一个字符串“Hello，world”这个事件交给订阅者的onNext()方法。第二：对于产生的这个字符串“Hello， world”而言，Observable就是一个事件观察者，它观察到了这个字符串的产生，然后将这个字符串产生的事件发送给了订阅者。
   Observer和Subscriber是一个东西，Subscriber继承自Observer。

   Subscription是一个接口，提供了对一个Subscriber进行取消订阅（unSubscribe）和是否取消订阅（isUnsubscribe）的功能，它的具体实现就是Subscriber。上例中的第三步中subscribe方法将会返回一个Subscription，用户可以方便的取消订阅。

5、其他的创建Observable的方式
     Observable.From(DataCollection) 使用一个数据集创建一个Observable，自动遍历发射集合中每条数据
     Observable.just(a java method return )  根据一个或多个其他的方法返回值创建一个Observable，因为just可以接受1-9各参数，然后按照参数顺序发射他们。也可以是一个数据集合，像from方法，但他是发射整个列表。

6、Subject = Observable + Observer （既是事件产生者，又是事件订阅者）
     Subject有四种类型 PublishSubject  BehaviorSubject     ReplaySubject     AsyncSubject

PublishSubject 是一个可以在任何时候发射事件的事件产生者，而不一定是在订阅者开始订阅的时候。
BehaviorSubject会首先向他的订阅者发送截至订阅前最新的一个数据对象（或初始值），然后正常发送订阅后的数据流。
ReplaySubject会缓存它所订阅的所有数据，向任意一个订阅它的观察者重发。
AsyncSubject只会发布最后一个数据 给已经订阅的每一个观察者。

7、操作符
   1、repeat()  对一个Observable重复发射数据
      例：

Observable.just(1, 2).repeat(5).subscribe(new Subscriber<Integer>() {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Integer integer) {
        System.out.println("integer------>" + integer);
    }
});

   2、defer() 延迟Observable的创建直到观察者订阅
     例：

private Observable<Long> getDeferObservable() {
    return Observable.defer(new Func0<Observable<Long>>() {
        @Override
        public Observable<Long> call() {
            return getJustObservable();
        }
    });
}

//每次生成新的observable
@Test
public void testDefer() {
    Observable<Long> observable = getDeferObservable();
    for (int i = 0; i < 10; i++) {
        observable.subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                System.out.println("aLong------>" + aLong);
            }
        });
    }
}

3、interval() 在指定的时间间隔内重复数字 0到正无穷
Subscription topeMePlease = Observable.interval(3, TimeUnit.SECONDS)
        .subscribe(new Observer<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                System.out.println("aLong---->"+aLong);
            }
        });

4、timer()  指定延迟时间指定间隔循环发射
Observable.timer(3, 100, TimeUnit.MILLISECONDS).subscribe(new Action1<Long>() {
    @Override
    public void call(Long aLong) {
        System.out.println("aLong------>" + aLong);
    }
});

5、filter()  过滤出符合要求的数据
     filter((appInfo) -> appInfo.getName().startWith(“C”)) //过滤出C开头的应用名称
6、take()  指定原始序列中的前几条数据发射
      take(3)
7、takeLast()  指定原始序列中的最后几条数据发射
       takeLast(3)
8、distinct()  去除重复数据  可以用来防止界面控件重复点击
9、distinctUntilChanged()  去除与上一个重复的值
10、first()和last() 发射原始序列中的第一个或最后一个值
11、firstOrDefault()和lastOrDefault()  当观测序列完成时发送默认值
12、skip()和skipLast()   不发射前N个值或者后N个值
13、ElementAt()   elementAtOrDefault()     发射指定位置的元素 ，如果没有就发送默认值
14、sample(30, TimeUnit.SECONDS)  在指定时间间隔内由Observable发射最近一次的数值  再加一个throttleFirst() 就是发射第一个而不是最近一个元素
15、timeout()   每隔一定时间发射至少一次数据，如果在指定时间间隔内没有得到一个值则发送一个错误
16、debounce()   过滤掉由Observable发射的速率过快的数据

17、map  指定一个fun对象，然后将它应用到每一个由Observable发射的值上
18、flatMap()  根据上一个Observable发射的数据生成新的Observable，注意新产生的Observable是平铺的，也就是说最终得到数据顺序是不定的，并且有一个产生error，此次调用就会结束
19、concatMap()   解决的flatMap()的交叉问题，能够把发射的值连续在一起，而不是合并他们
20、scan()   累加器  对原始Observable发射的每一项数据都应用一个函数，计算出函数的结果值，并将该值填回可观测序列，等待和下一次发射的数据一起使用。
例：
Observable.just(1,2,3,4,5)
               .scan((sum, item) -> sum + item)
               .subscribe(new Subscriber<Interger> () {
     @Override
     public void onCompleted() {

     }

     @Override
     public void onError(Throwable e) {

     }

     @Override
     public void onNext(Integer integer) {
         System.out.println("integer------>" + integer);
     }
         });
输出结果为：1   3  6  10  15
（这个操作符可用来对数据进行排序）

21、groupBy()  将原Observable变换成哼一个发射Observables的新的Observable。他们中的每一个新的Observable都发射一组指定的数据

22、buffer()    将原Observable变换一个新的Observable，这个新的Observable每次发射一组列表而不是一个个发射

23、merge()   多个Observable合并成一个最终发射的Observable  （多个Observable发射的数据类型一般相同）

24、zip   合并多个Observable数据，生成新的数据

8、调度器
     RxJava提供了5种调度器：
          .io()  .computation()  .immediate()  .newThread()  .trampoline()
   Schedulers.io()  专用于io操作，但是大量的io操作会创建多个线程并占用内存
   Schedulers.computation()  计算工作默认的调度器，与io无关
   Schedulers.immedidate()  在当前线程立即执行指定的工作
   Schedulers.newThread()   为指定任务启动一个新的线程
   schedulers.tramppline()  把要执行的任务加入到当前线程任务队列中，调度器会顺序执行队列中的任务

   SubscribeOn(Schedulers.io())  指定任务工作线程
   ObserveOn(AndroidSchedulers.mainThread())  指定观察者处理返回结果所在线程为ui线程
