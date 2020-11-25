package com.katyshevtseva.kikiorg.core.sections.finance;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {
    @Getter
    @Setter
    private Owner currentOwner = Owner.C;

    public enum Owner {
        K("Екатерина"), M("Камиль"), C("Семья");

        private String name;

        Owner(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
