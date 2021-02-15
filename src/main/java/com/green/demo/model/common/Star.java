package com.green.demo.model.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Embeddable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Embeddable
@Getter
@NoArgsConstructor
public class Star {

    private int star;

    public Star(int star) {
        checkNotNull(star, "star must be provided.");
        checkArgument(
                star >= 0 && star <= 5,
                "star must be between 0 and 5"
        );

        this.star = star;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Star star1 = (Star) o;

        return new EqualsBuilder().append(star, star1.star).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(star).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("star", star)
                .toString();
    }
}
