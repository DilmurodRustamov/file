package uz.developer.entity.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.developer.entity.attachment.Attachment;
import uz.developer.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ProductEntity extends BaseEntity {

    private Double price;
    @Column(unique = true)
    private String name;
    private int quantity;

    @OneToMany
    private List<Attachment> attachments;
}
