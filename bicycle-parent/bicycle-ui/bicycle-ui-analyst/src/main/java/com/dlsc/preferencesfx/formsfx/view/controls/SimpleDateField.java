package com.dlsc.preferencesfx.formsfx.view.controls;

import com.dlsc.formsfx.model.structure.DataField;
import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.format.FormatStyle;
import java.util.Locale;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.converter.LocalDateStringConverter;

public class SimpleDateField extends DataField<ObjectProperty<LocalDate>, LocalDate, SimpleDateField> {
    
    public SimpleDateField(ObjectProperty<LocalDate> binding) {
        super(new SimpleObjectProperty<>(binding.getValue()), new SimpleObjectProperty<>(binding.getValue()));
        bind(binding);
        Chronology chronology = Chronology.ofLocale(Locale.getDefault(Locale.Category.FORMAT));
        stringConverter = new LocalDateStringConverter(FormatStyle.SHORT, null, chronology);
        rendererSupplier = () -> new SimpleDateControl(binding.get());
        userInput.setValue(null);
        userInput.setValue(stringConverter.toString((LocalDate) persistentValue.getValue()));
    }

    @Override
    public SimpleDateField bind(ObjectProperty<LocalDate> binding) {
        return super.bind(binding);
    }
}
