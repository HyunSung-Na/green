package com.green.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Embeddable;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.regex.Pattern.matches;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Embeddable
@Getter
@NoArgsConstructor
public class Name {
    
    private String name;
    
    public Name(String name) {
        checkArgument(isNotEmpty(name), "name must be provided.");
        checkArgument(
                name.length() >= 4 && name.length() <= 16,
                "name length must be between 4 and 16 characters."
        );
        checkArgument(checkName(name), "Invalid name: " + name);

        this.name = name;
    }

    private boolean checkName(String name) {
        return matches("^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$", name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Name name1 = (Name) o;

        return new EqualsBuilder().append(name, name1.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(name).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .toString();
    }
}
