package africanroyals.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Sale")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SaleId")
    private Long id;

    // SaleDate is not in the database schema, so we'll compute it or leave it null
    // Using @Transient so Hibernate doesn't try to map it to a column
    @Transient
    private LocalDateTime saleDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserId", nullable = false)
    private User user;

    @Column(name = "SubTotal", nullable = false)
    private Double subTotal;

    @Column(name = "Tax", nullable = false)
    private Double tax;

    @Column(name = "ShippingMethod")
    private String shippingMethod;

    @Column(name = "ShippingCost", nullable = false)
    private Double shippingCost;

    @Column(name = "ShippingDetails")
    private String shippingDetails;

    @Column(name = "ShippingAddress")
    private String shippingAddress;

    @Column(name = "ShippingCity")
    private String shippingCity;

    @Column(name = "ShippingState")
    private String shippingState;

    @Column(name = "ShippingZip")
    private String shippingZip;

    @Column(name = "Total", nullable = false)
    private double total;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "Sale_Inventory_Item",
        joinColumns = @JoinColumn(name = "SaleId"),
        inverseJoinColumns = @JoinColumn(name = "ItemId")
    )
    private List<InventoryItem> items = new ArrayList<>();

    //Constructors
    public Sale() {
    }

    public Sale(User user, LocalDateTime saleDate, double total) {
        this.user = user;
        this.saleDate = saleDate;
        this.total = total;
    }

    public Sale(User user, LocalDateTime saleDate, Double subTotal, Double tax, 
                String shippingMethod, Double shippingCost, String shippingDetails,
                String shippingAddress, String shippingCity, String shippingState, 
                String shippingZip, double total) {
        this.user = user;
        this.saleDate = saleDate;
        this.subTotal = subTotal;
        this.tax = tax;
        this.shippingMethod = shippingMethod;
        this.shippingCost = shippingCost;
        this.shippingDetails = shippingDetails;
        this.shippingAddress = shippingAddress;
        this.shippingCity = shippingCity;
        this.shippingState = shippingState;
        this.shippingZip = shippingZip;
        this.total = total;
    }

    //Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<InventoryItem> getItems() {
        return items;
    }

    public void setItems(List<InventoryItem> items) {
        this.items = items;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public Double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(Double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getShippingDetails() {
        return shippingDetails;
    }

    public void setShippingDetails(String shippingDetails) {
        this.shippingDetails = shippingDetails;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    public String getShippingState() {
        return shippingState;
    }

    public void setShippingState(String shippingState) {
        this.shippingState = shippingState;
    }

    public String getShippingZip() {
        return shippingZip;
    }

    public void setShippingZip(String shippingZip) {
        this.shippingZip = shippingZip;
    }
}
