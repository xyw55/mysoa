namespace java com.xyw55.thrift.gencode.server
include 'UserModel.thrift'

service HelloService{
    string sayHello(1:UserModel.User user);
}