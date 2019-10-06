package application.resources.model;

public class Product {

    private String id, main_id, name, main_name, fecha, prov_RUC, category, representation;
    private int internal_quantity;
    private double stock;
    private double box_price, unit_price;
    private String tax;
    private double box_12, unit_12, sell_box, sell_unit;

    public Product(){}

    public Product(String id, String main_id, String name, String main_name, String fecha, String prov_RUC,
                   String category, String representation, int internal_quantity, double stock, double box_price,
                   double unit_price, String tax, double box_12, double unit_12, double sell_box, double sell_unit) {
        this.id = id;
        this.main_id = main_id;
        this.name = name;
        this.main_name = main_name;
        this.fecha = fecha;
        this.prov_RUC = prov_RUC;
        this.category = category;
        this.representation = representation;
        this.internal_quantity = internal_quantity;
        this.stock = stock;
        this.box_price = box_price;
        this.unit_price = unit_price;
        this.tax = tax;
        this.box_12 = box_12;
        this.unit_12 = unit_12;
        this.sell_box = sell_box;
        this.sell_unit = sell_unit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMain_id() {
        return main_id;
    }

    public void setMain_id(String main_id) {
        this.main_id = main_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMain_name() {
        return main_name;
    }

    public void setMain_name(String main_name) {
        this.main_name = main_name;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getProv_RUC() {
        return prov_RUC;
    }

    public void setProv_RUC(String prov_RUC) {
        this.prov_RUC = prov_RUC;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRepresentation() {
        return representation;
    }

    public void setRepresentation(String representation) {
        this.representation = representation;
    }

    public int getInternal_quantity() {
        return internal_quantity;
    }

    public void setInternal_quantity(int internal_quantity) {
        this.internal_quantity = internal_quantity;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public double getBox_price() {
        return box_price;
    }

    public void setBox_price(double box_price) {
        this.box_price = box_price;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public double getBox_12() {
        return box_12;
    }

    public void setBox_12(double box_12) {
        this.box_12 = box_12;
    }

    public double getUnit_12() {
        return unit_12;
    }

    public void setUnit_12(double unit_12) {
        this.unit_12 = unit_12;
    }

    public double getSell_box() {
        return sell_box;
    }

    public void setSell_box(double sell_box) {
        this.sell_box = sell_box;
    }

    public double getSell_unit() {
        return sell_unit;
    }

    public void setSell_unit(double sell_unit) {
        this.sell_unit = sell_unit;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", main_id='" + main_id + '\'' +
                ", name='" + name + '\'' +
                ", main_name='" + main_name + '\'' +
                ", fecha='" + fecha + '\'' +
                ", prov_RUC='" + prov_RUC + '\'' +
                ", category='" + category + '\'' +
                ", representation='" + representation + '\'' +
                ", tax='" + tax + '\'' +
                ", internal_quantity=" + internal_quantity +
                ", stock=" + stock +
                ", box_price=" + box_price +
                ", unit_price=" + unit_price +
                ", box_12=" + box_12 +
                ", unit_12=" + unit_12 +
                ", sell_box=" + sell_box +
                ", sell_unit=" + sell_unit +
                '}';
    }
}
