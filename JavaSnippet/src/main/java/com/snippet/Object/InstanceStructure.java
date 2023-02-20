

public class InstanceStructure {
  
    public static void main(String[] args) {
      
        OrderModel orderModel = new OrderModel();
        ClassLayout classLayout1 = ClassLayout.parseInstance(orderModel);
        String emptyOrder = classLayout1.toPrintable();
        System.out.println(emptyOrder);

        OrderModel order = new OrderModel();
        order.setOrderId(1234562314544L);
        order.setUserInfo(new UserModel());
        ClassLayout classLayout2 = ClassLayout.parseInstance(order);
        String orderMemoryInfo = classLayout2.toPrintable();
        System.out.println(orderMemoryInfo);

        OrderModel orderWithUser = new OrderModel();
        orderWithUser.setOrderId(123456456789L);
        orderWithUser.setOrderInfo("info");
        orderWithUser.setUserInfo(new UserModel(456789123123412L, "God"));
        ClassLayout classLayout3 = ClassLayout.parseInstance(orderWithUser);
        String orderUserMemoryInfo = classLayout3.toPrintable();
        System.out.println(orderUserMemoryInfo);

        Product product = new Product(1111L, "xxx", 2222, "ddd");
        ClassLayout classLayout4 = ClassLayout.parseInstance(product);
        String productMemoryInfo = classLayout4.toPrintable();
        System.out.println(productMemoryInfo);
    }
}
