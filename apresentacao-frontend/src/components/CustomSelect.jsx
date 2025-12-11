import React, { useState, useRef, useEffect } from "react";

/*
  CustomSelect (lightweight)
  - options: [{ value, label }]
  - value (optional)
  - onChange(value)
  - placeholder
  Notes:
  - No caret in trigger (per request)
  - When opened, selected item shows only a vertical bar on the left
  - Keyboard nav not implemented (can add later)
*/
export default function CustomSelect({
  options = [],
  value: valueProp = null,
  onChange,
  placeholder = "Selecione",
}) {
  const [open, setOpen] = useState(false);
  const [selected, setSelected] = useState(() => {
    if (valueProp != null)
      return options.find((o) => o.value === valueProp) || null;
    return options[0] || null;
  });

  useEffect(() => {
    if (valueProp != null) {
      setSelected(options.find((o) => o.value === valueProp) || null);
    }
  }, [valueProp, options]);

  const ref = useRef(null);

  useEffect(() => {
    function onDoc(e) {
      if (!ref.current) return;
      if (!ref.current.contains(e.target)) setOpen(false);
    }
    document.addEventListener("mousedown", onDoc);
    return () => document.removeEventListener("mousedown", onDoc);
  }, []);

  function handleSelect(opt) {
    setSelected(opt);
    setOpen(false);
    if (onChange) onChange(opt.value);
  }

  return (
    <div className={`custom-select ${open ? "open" : ""}`} ref={ref}>
      <button
        type="button"
        className="custom-select__trigger"
        aria-haspopup="listbox"
        aria-expanded={open}
        onClick={() => setOpen((s) => !s)}
      >
        <span className="custom-select__value">
          {selected ? selected.label : placeholder}
        </span>
        {/* caret intentionally removed */}
      </button>

      <ul
        role="listbox"
        tabIndex={-1}
        className="custom-select__list"
        aria-hidden={!open}
      >
        {options.map((opt) => {
          const isSelected = selected && selected.value === opt.value;
          return (
            <li
              key={opt.value}
              role="option"
              aria-selected={isSelected}
              className={`custom-select__item ${isSelected ? "selected" : ""}`}
              onClick={() => handleSelect(opt)}
            >
              <div className="item-content">
                <span className="item-label">{opt.label}</span>
                {/* removed check icon per request (only vertical bar indicates selection) */}
              </div>
            </li>
          );
        })}
      </ul>
    </div>
  );
}
