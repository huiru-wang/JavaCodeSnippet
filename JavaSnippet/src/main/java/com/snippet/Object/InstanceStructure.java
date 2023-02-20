

public class InstanceStructure {
  
    public static void main(String[] args) {
      
        OrderModel orderModel = new OrderModel();
        ClassLayout classLayout1 = ClassLayout.parseInstance(orderModel);
        String emptyOrder = classLayout1.toPrintable();
        System.out.println(emptyOrder);

        OrderModel order = new OrderModel();
        order.setOrderId(123456L);
        order.setUserInfo(new UserModel());
        ClassLayout classLayout2 = ClassLayout.parseInstance(order);
        String orderMemoryInfo = classLayout2.toPrintable();
        System.out.println(orderMemoryInfo);

        OrderModel orderWithUser = new OrderModel();
        orderWithUser.setOrderId(123456L);
        orderWithUser.setUserInfo(new UserModel(456789L, "God"));
        ClassLayout classLayout3 = ClassLayout.parseInstance(orderWithUser);
        String orderUserMemoryInfo = classLayout3.toPrintable();
        System.out.println(orderUserMemoryInfo);
    }
}
