package com.ignitedev.devsequipmenteffects.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

@NoArgsConstructor( access = AccessLevel.PRIVATE )
public class BaseUtil {
    
    public static Component colorComponent(String toColor) {
        
        return LegacyComponentSerializer.legacyAmpersand().deserialize(toColor).asComponent();
    }
    
}
