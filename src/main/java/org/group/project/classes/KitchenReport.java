package org.group.project.classes;

import org.group.project.exceptions.TextFileNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class reports on most popular menu items and outstanding orders.
 *
 * @author azmi_maz
 */
public class KitchenReport extends Report {
    private static final String MOST_POPULAR_ITEM = "most popular item";
    private static final String OUTSTANDING_ORDERS = "outstanding orders";
    private static final String MOST_ACTIVE_CUSTOMER = "most active customer";
    private static final String FOOD_TYPE = "food";
    private static final String DRINK_TYPE = "drink";
    private static final String TAKEAWAY_TYPE = "takeaway";
    private static final String DELIVERY_TYPE = "delivery";
    private static final String DINEIN_TYPE = "dinein";
    private static final String PENDING_KITCHEN = "pending-kitchen";
    private static final String DISH_REMARK = "dish";
    private static final String COCKTAIL_REMARK = "cocktail";
    private static final String MOST_ORDERED_FOOD_REPORT = "The restaurant's " +
            "most popular dish is %s, while the " +
            "bar's top choice during happy hours is the %s."
            + System.lineSeparator();
    private static final String MOST_ACTIVE_CUSTOMER_REPORT = "%s is a " +
            "loyal customer who has placed a total of %d " +
            "orders. They have a deep appreciation for the " +
            "culinary arts, especially when it comes to " +
            "indulging in delectable treats like %s.";
    private static final String MOST_POPULAR_ITEM_REPORT = "The %s is a " +
            "highly sought-after %s, with a remarkable " +
            "%d orders." + System.lineSeparator();
    private static final String OUTSTANDING_ORDERS_REPORT = "The kitchen " +
            "is bustling, with %d tickets to fulfil. " +
            "Let's make sure the kitchen doesn't experience " +
            "burnout. Give priority to %d dine-in orders, " +
            "as they are our top sellers, followed by %d " +
            "takeaways, particularly during lunch. Lastly," +
            " focus on increasing our sales through %d " +
            "deliveries.";
    private static final String ANALYSIS_ON_ORDER_TYPES = "The total number " +
            "of orders is %d. Customers in the area " +
            "have been placing a large number of " +
            "takeaway orders, with a total of %d orders so " +
            "far, and %s is the top choice. During " +
            "the evenings, the number of delivery orders " +
            "reaches a total of %d, and it seems that %s is " +
            "a popular option among customers. On weekends, " +
            "our customers who have a passion for food love " +
            "to come in and savour a delicious meal with us. " +
            "We usually get about %d dine-in orders, and it " +
            "appears that %s is the preferred choice among " +
            "our customers.";

    /**
     * This constructor creates a new report with timestamp.
     *
     * @param reportType - the type of the report.
     * @param user       - the user who generated the report.
     */
    public KitchenReport(String reportType,
                         User user) throws TextFileNotFoundException {

        super(reportType, user);
        if (reportType.equalsIgnoreCase(
                MOST_POPULAR_ITEM)) {
            super.setReportData(getMostPopularMenuItemsReport());
        } else if (reportType.equalsIgnoreCase(
                OUTSTANDING_ORDERS)) {
            super.setReportData(getOutstandingOrdersReport());
        } else if (reportType.equalsIgnoreCase(
                MOST_ACTIVE_CUSTOMER)) {
            super.setReportData(getMostActiveCustomerReport());
        }

    }

    /**
     * This method generates the most popular menu items report.
     *
     * @return the report for most popular menu items.
     */
    public String getMostPopularMenuItemsReport()
            throws TextFileNotFoundException {
        try {
            return prepareReportData(MOST_POPULAR_ITEM);
        } catch (TextFileNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method generates the outstanding orders report.
     *
     * @return the report for outstanding orders.
     */
    public String getOutstandingOrdersReport()
            throws TextFileNotFoundException {
        try {
            return prepareReportData(OUTSTANDING_ORDERS);
        } catch (TextFileNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method generates the most active customer report.
     *
     * @return the report for the most active customer.
     * @throws TextFileNotFoundException - if text file is non-existent.
     */
    public String getMostActiveCustomerReport()
            throws TextFileNotFoundException {
        try {
            return prepareReportData(MOST_ACTIVE_CUSTOMER);
        } catch (TextFileNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This method prepares the kitchen report based on report type.
     *
     * @param reportChoice - the type of report that is selected.
     * @return - the specific report based on selected type.
     * @throws TextFileNotFoundException - if text file is non-existent.
     */
    public String prepareReportData(
            String reportChoice
    ) throws TextFileNotFoundException {

        try {

            Kitchen kitchen = new Kitchen();
            List<Order> orderList = kitchen.getAllOrderTickets();

            if (orderList == null) {
                return null;
            }

            // Most popular item
            Map<String, Integer> itemCountMap = new HashMap<>();
            // Most active customer
            Map<Integer, Integer> customerCountMap = new HashMap<>();
            // Most ordered food
            Map<String, Integer> foodOnlyMap = new HashMap<>();
            // Most ordered drink
            Map<String, Integer> drinkOnlyMap = new HashMap<>();
            // Most ordered item in takeaway orders
            Map<String, Integer> takeAwayOrderItemMap = new HashMap<>();
            // Most ordered item in delivery orders
            Map<String, Integer> deliveryOrderItemMap = new HashMap<>();
            // Most ordered item in dinein orders
            Map<String, Integer> dineOrderItemMap = new HashMap<>();
            // Number of outstanding orders by type
            Map<String, Integer> outstandingOrderMap = new HashMap<>();
            for (Order order : orderList) {
                List<FoodDrink> foodsOrdered = order.getOrderedFoodDrinkLists();
                String orderType = order.getOrderType();
                for (FoodDrink item : foodsOrdered) {
                    String itemName = item.getItemName();
                    if (itemCountMap.containsKey(itemName)) {
                        int currentCount = itemCountMap.get(itemName);
                        currentCount += item.getQuantity();
                        itemCountMap.put(itemName, currentCount);
                    } else {
                        itemCountMap.put(itemName, item.getQuantity());
                    }

                    String itemType = item.getItemType();
                    if (foodOnlyMap.containsKey(itemName)
                            && itemType.equalsIgnoreCase(FOOD_TYPE)) {
                        int getFoodCount = foodOnlyMap.get(itemName);
                        getFoodCount += item.getQuantity();
                        foodOnlyMap.put(itemName, getFoodCount);
                    } else {
                        foodOnlyMap.put(itemName, item.getQuantity());
                    }

                    if (drinkOnlyMap.containsKey(itemName)
                            && itemType.equalsIgnoreCase(DRINK_TYPE)) {
                        int getFoodCount = foodOnlyMap.get(itemName);
                        getFoodCount += item.getQuantity();
                        drinkOnlyMap.put(itemName, getFoodCount);
                    } else {
                        drinkOnlyMap.put(itemName, item.getQuantity());
                    }

                    if (takeAwayOrderItemMap.containsKey(itemName)
                            && orderType.equalsIgnoreCase(
                            TAKEAWAY_TYPE)) {
                        int getFoodCount = takeAwayOrderItemMap.get(itemName);
                        getFoodCount += item.getQuantity();
                        takeAwayOrderItemMap.put(itemName, getFoodCount);
                    } else {
                        takeAwayOrderItemMap.put(itemName, item.getQuantity());
                    }

                    if (deliveryOrderItemMap.containsKey(itemName)
                            && orderType.equalsIgnoreCase(
                            DELIVERY_TYPE)) {
                        int getFoodCount = deliveryOrderItemMap.get(itemName);
                        getFoodCount += item.getQuantity();
                        deliveryOrderItemMap.put(itemName, getFoodCount);
                    } else {
                        deliveryOrderItemMap.put(itemName, item.getQuantity());
                    }

                    if (dineOrderItemMap.containsKey(itemName)
                            && orderType.equalsIgnoreCase(
                            DINEIN_TYPE)) {
                        int getFoodCount = dineOrderItemMap.get(itemName);
                        getFoodCount += item.getQuantity();
                        dineOrderItemMap.put(itemName, getFoodCount);
                    } else {
                        dineOrderItemMap.put(itemName, item.getQuantity());
                    }
                }

                int customerId = order.getCustomer().getCustomerId();
                if (customerCountMap.containsKey(customerId)) {
                    int currentCustomerCount = customerCountMap.get(customerId);
                    currentCustomerCount++;
                    customerCountMap.put(customerId, currentCustomerCount);
                } else {
                    customerCountMap.put(customerId, 1);
                }

                if (order.getOrderStatus().equalsIgnoreCase(
                        PENDING_KITCHEN)) {
                    if (outstandingOrderMap.containsKey(orderType)) {
                        int currentCount = outstandingOrderMap.get(orderType);
                        currentCount++;
                        outstandingOrderMap.put(orderType, currentCount);
                    } else {
                        outstandingOrderMap.put(orderType, 1);
                    }
                }

            }

            String mostPopularItem = "";
            int numOfMostPopularItem = 0;
            for (Map.Entry<String, Integer> entry : itemCountMap.entrySet()) {
                String currentItemName = entry.getKey();
                int currentItemCounter = entry.getValue();
                if (numOfMostPopularItem < currentItemCounter) {
                    numOfMostPopularItem = currentItemCounter;
                    mostPopularItem = currentItemName;
                } else if (numOfMostPopularItem == 0) {
                    numOfMostPopularItem = currentItemCounter;
                    mostPopularItem = currentItemName;
                }
            }

            int mostActiveCustomerId = 0;
            int mostActiveCustomerCounter = 0;
            for (Map.Entry<Integer, Integer> entry :
                    customerCountMap.entrySet()) {
                int currentId = entry.getKey();
                int currentCounter = entry.getValue();
                if (mostActiveCustomerCounter < currentCounter) {
                    mostActiveCustomerCounter = currentCounter;
                    mostActiveCustomerId = currentId;
                } else if (mostActiveCustomerCounter == 0) {
                    mostActiveCustomerCounter = currentCounter;
                    mostActiveCustomerId = currentId;
                }
            }

            Map<String, Integer> mostActiveCustomerMap = new HashMap<>();
            for (Order order : orderList) {
                int currentCustomerId = order.getCustomer().getCustomerId();
                List<FoodDrink> itemList = order.getOrderedFoodDrinkLists();
                if (currentCustomerId == mostActiveCustomerId) {
                    for (FoodDrink item : itemList) {
                        String itemName = item.getItemName();
                        int itemQty = item.getQuantity();
                        if (mostActiveCustomerMap.containsKey(itemName)) {
                            int currentQty = mostActiveCustomerMap
                                    .get(itemName);
                            currentQty += itemQty;
                            mostActiveCustomerMap.put(itemName, currentQty);
                        } else {
                            mostActiveCustomerMap.put(
                                    itemName, itemQty
                            );
                        }
                    }
                }
            }

            String mostActiveCustomerFavouriteItem = "";
            int mostActiveCustomerFavItemCounter = 0;
            for (Map.Entry<String, Integer> entry :
                    mostActiveCustomerMap.entrySet()) {
                String itemName = entry.getKey();
                int itemQty = entry.getValue();
                if (mostActiveCustomerFavItemCounter < itemQty) {
                    mostActiveCustomerFavItemCounter = itemQty;
                    mostActiveCustomerFavouriteItem = itemName;
                } else if (mostActiveCustomerFavItemCounter == 0) {
                    mostActiveCustomerFavItemCounter = itemQty;
                    mostActiveCustomerFavouriteItem = itemName;
                }
            }

            String mostOrderedFoodItem = "";
            int mostOrderedFoodItemCounter = 0;
            for (Map.Entry<String, Integer> entry : foodOnlyMap.entrySet()) {
                String currentFood = entry.getKey();
                int currentCounter = entry.getValue();
                if (mostOrderedFoodItemCounter < currentCounter) {
                    mostOrderedFoodItemCounter = currentCounter;
                    mostOrderedFoodItem = currentFood;
                } else if (mostOrderedFoodItemCounter == 0) {
                    mostOrderedFoodItemCounter = currentCounter;
                    mostOrderedFoodItem = currentFood;
                }
            }

            String mostOrderedDrinkItem = "";
            int mostOrderedDrinkCounter = 0;
            for (Map.Entry<String, Integer> entry : drinkOnlyMap.entrySet()) {
                String currentDrink = entry.getKey();
                int currentCounter = entry.getValue();
                if (mostOrderedDrinkCounter < currentCounter) {
                    mostOrderedDrinkCounter = currentCounter;
                    mostOrderedDrinkItem = currentDrink;
                } else if (mostOrderedDrinkCounter == 0) {
                    mostOrderedDrinkCounter = currentCounter;
                    mostOrderedDrinkItem = currentDrink;
                }
            }

            int numOfTakeawayOrders = 0;
            int numOfDeliveryOrders = 0;
            int numOfDineinOrders = 0;

            String mostOrderedTakeawayItem = "";
            int mostOrderedTakeawayItemCounter = 0;
            for (Map.Entry<String, Integer> entry :
                    takeAwayOrderItemMap.entrySet()) {
                String currentItem = entry.getKey();
                int currentCounter = entry.getValue();
                numOfTakeawayOrders++;
                if (mostOrderedTakeawayItemCounter < currentCounter) {
                    mostOrderedTakeawayItemCounter = currentCounter;
                    mostOrderedTakeawayItem = currentItem;
                } else if (mostOrderedTakeawayItemCounter == 0) {
                    mostOrderedTakeawayItemCounter = currentCounter;
                    mostOrderedTakeawayItem = currentItem;
                }
            }

            String mostOrderedDeliveryItem = "";
            int mostOrderedDeliveryItemCounter = 0;
            for (Map.Entry<String, Integer> entry :
                    deliveryOrderItemMap.entrySet()) {
                String currentItem = entry.getKey();
                int currentCounter = entry.getValue();
                numOfDeliveryOrders++;
                if (mostOrderedDeliveryItemCounter < currentCounter) {
                    mostOrderedDeliveryItemCounter = currentCounter;
                    mostOrderedDeliveryItem = currentItem;
                } else if (mostOrderedDeliveryItemCounter == 0) {
                    mostOrderedDeliveryItemCounter = currentCounter;
                    mostOrderedDeliveryItem = currentItem;
                }
            }

            String mostOrderedDineinItem = "";
            int mostOrderedDineinItemCounter = 0;
            for (Map.Entry<String, Integer> entry :
                    dineOrderItemMap.entrySet()) {
                String currentItem = entry.getKey();
                int currentCounter = entry.getValue();
                numOfDineinOrders++;
                if (mostOrderedDineinItemCounter < currentCounter) {
                    mostOrderedDineinItemCounter = currentCounter;
                    mostOrderedDineinItem = currentItem;
                } else if (mostOrderedDineinItemCounter == 0) {
                    mostOrderedDineinItemCounter = currentCounter;
                    mostOrderedDineinItem = currentItem;
                }
            }

            int numOfTotalOrders = numOfTakeawayOrders
                    + numOfDeliveryOrders
                    + numOfDineinOrders;

            int totalNumOfOutstandingOrders = 0;
            int numOfOutstandingDinein = 0;
            int numOfOutstandingTakeaway = 0;
            int numOfOutstandingDelivery = 0;
            for (Map.Entry<String, Integer> entry :
                    outstandingOrderMap.entrySet()) {
                String currentItemType = entry.getKey();
                int currentCounter = entry.getValue();
                if (currentItemType.equalsIgnoreCase(DINEIN_TYPE)) {
                    numOfOutstandingDinein = currentCounter;
                } else if (currentItemType.equalsIgnoreCase(
                        TAKEAWAY_TYPE)) {
                    numOfOutstandingTakeaway = currentCounter;
                } else {
                    numOfOutstandingDelivery = currentCounter;
                }
            }

            totalNumOfOutstandingOrders = numOfOutstandingDinein
                    + numOfOutstandingTakeaway
                    + numOfOutstandingDelivery;

            Menu menu = new Menu();
            String typeOfPopularItem = menu.findTypeByItemName(mostPopularItem);
            String mostPopularItemReport = String.format(
                    MOST_POPULAR_ITEM_REPORT,
                    mostPopularItem,
                    typeOfPopularItem.equalsIgnoreCase(FOOD_TYPE) ?
                            DISH_REMARK : COCKTAIL_REMARK,
                    numOfMostPopularItem
            );

            String mostActiveCustomerName = "";
            UserManagement userManagement = new UserManagement();
            Customer mostActiveCustomer = userManagement
                    .getCustomerById(
                            mostActiveCustomerId
                    );
            mostActiveCustomerName = mostActiveCustomer
                    .getDataForListDisplay();

            String mostActiveCustomerReport = String.format(
                    MOST_ACTIVE_CUSTOMER_REPORT,
                    mostActiveCustomerName,
                    mostActiveCustomerCounter,
                    mostActiveCustomerFavouriteItem
            );

            String mostOrderedFoodReport = String.format(
                    MOST_ORDERED_FOOD_REPORT,
                    mostOrderedFoodItem,
                    mostOrderedDrinkItem
            );

            String analysisOnOrderTypeReport = String.format(
                    ANALYSIS_ON_ORDER_TYPES,
                    numOfTotalOrders,
                    numOfTakeawayOrders,
                    mostOrderedTakeawayItem,
                    numOfDeliveryOrders,
                    mostOrderedDeliveryItem,
                    numOfDineinOrders,
                    mostOrderedDineinItem
            );

            String outstandingOrdersByType = String.format(
                    OUTSTANDING_ORDERS_REPORT,
                    totalNumOfOutstandingOrders,
                    numOfOutstandingDinein,
                    numOfOutstandingTakeaway,
                    numOfOutstandingDelivery
            );

            if (reportChoice.equalsIgnoreCase(
                    MOST_POPULAR_ITEM)) {
                return mostPopularItemReport
                        + mostOrderedFoodReport
                        + analysisOnOrderTypeReport;
            } else if (reportChoice.equalsIgnoreCase(
                    OUTSTANDING_ORDERS)) {
                return outstandingOrdersByType;
            } else if (reportChoice.equalsIgnoreCase(
                    MOST_ACTIVE_CUSTOMER)) {
                return mostActiveCustomerReport;
            }
            return null;

        } catch (TextFileNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
