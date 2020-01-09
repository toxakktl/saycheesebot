package kz.ata.saycheese.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "expenses")
@SequenceGenerator(name = "id_gen", sequenceName = "expenses_seq", initialValue = 1000, allocationSize = 1)
public class Expenses extends BaseModel {

    @Column(name = "date")
    private Date date;

    @Column(name = "amount")
    private BigDecimal amount;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
