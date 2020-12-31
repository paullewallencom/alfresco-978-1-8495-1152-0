<import resource="classpath:alfresco/extension/templates/webscripts/com/packtpub/aws3/samples/ch07/fib.js">

model.fib = [ fib(1), fib(2) , fib(3), fib(4), fib(5), fib(6), fib(7), fib(8), fib(9), fib(10) ]

if (logger.isLoggingEnabled()) {
    logger.log("fib = " + model.fib)    
}
