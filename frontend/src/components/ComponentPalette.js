import React, { useState, useEffect, useRef } from 'react';
import './ComponentPalette.css';
import { componentAPI } from '../api';
import {
  FaDatabase,
  FaMemory,
  FaNetworkWired,
  FaStream,
  FaHdd,
  FaBalanceScale,
  FaBolt,
  FaLayerGroup,
  FaGlobe,
  FaLaptop,
  FaCheck,
} from 'react-icons/fa';

const COMPONENT_TYPES = [
  { type: 'DATABASE', icon: <FaDatabase />, label: 'Database', color: '#10b981' },
  { type: 'CACHE', icon: <FaMemory />, label: 'Cache', color: '#f59e0b' },
  { type: 'API_SERVICE', icon: <FaNetworkWired />, label: 'API Service', color: '#3b82f6' },
  { type: 'QUEUE', icon: <FaStream />, label: 'Queue', color: '#8b5cf6' },
  { type: 'STORAGE', icon: <FaHdd />, label: 'Storage', color: '#ec4899' },
  { type: 'LOAD_BALANCER', icon: <FaBalanceScale />, label: 'Load Balancer', color: '#06b6d4' },
  { type: 'STREAM_PROCESSOR', icon: <FaBolt />, label: 'Stream Processor', color: '#6366f1' },
  { type: 'BATCH_PROCESSOR', icon: <FaLayerGroup />, label: 'Batch Processor', color: '#84cc16' },
  { type: 'EXTERNAL_SERVICE', icon: <FaGlobe />, label: 'External Service', color: '#64748b' },
  { type: 'CLIENT', icon: <FaLaptop />, label: 'Client', color: '#0ea5e9' },
];

const ComponentPalette = ({ onPreviewSubtype }) => {
  const [subtypesByType, setSubtypesByType] = useState({});
  const [loadingByType, setLoadingByType] = useState({});
  const [selectedByType, setSelectedByType] = useState({});
  const [openDropdown, setOpenDropdown] = useState(null);
  const [pinnedType, setPinnedType] = useState(null); // keep panel visible when pinned
  const panelRef = useRef(null);

  const formatSubtypeName = (subtype) =>
    String(subtype || '')
      .replace(/_/g, ' ')
      .toLowerCase()
      .replace(/\b\w/g, (c) => c.toUpperCase());

  const normalizeSubtypes = (list) =>
    (list || []).map((item) => {
      if (!item) return null;
      if (typeof item === 'string') {
        return { id: item, name: item, label: formatSubtypeName(item), heuristics: null };
      }
      const id = item.name || item.id || item.type || JSON.stringify(item);
      const name = item.name || item.id || item.type || id;
      const heuristics = item.heuristics || item.description || item.note || null;
      return { id, name, label: formatSubtypeName(name), heuristics };
    }).filter(Boolean);

  // prefetch subtypes for snappy UX
  useEffect(() => {
    COMPONENT_TYPES.forEach((c) => {
      const t = c.type;
      (async () => {
        setLoadingByType((s) => ({ ...s, [t]: true }));
        try {
          const resp = await componentAPI.getSubtypes(t);
          const normalized = normalizeSubtypes(resp?.data?.subtypes || []);
          // fetch heuristics for each subtype where possible
          const withHeuristics = await Promise.all(
            (normalized.length ? normalized : [{ id: 'DEFAULT', name: 'DEFAULT', label: 'Default', heuristics: null }]).map(async (st) => {
              try {
                const hResp = await componentAPI.getHeuristicsForSubtype(t, st.id);
                return { ...st, heuristics: hResp.data };
              } catch (e) {
                return st;
              }
            })
          );

          const final = withHeuristics.length ? withHeuristics : [{ id: 'DEFAULT', name: 'DEFAULT', label: 'Default', heuristics: null }];
          setSubtypesByType((s) => ({ ...s, [t]: final }));
          setSelectedByType((s) => ({ ...s, [t]: final[0].id }));
        } catch (err) {
          const fallback = [{ id: 'DEFAULT', name: 'DEFAULT', label: 'Default', heuristics: null }];
          setSubtypesByType((s) => ({ ...s, [t]: fallback }));
          setSelectedByType((s) => ({ ...s, [t]: 'DEFAULT' }));
        } finally {
          setLoadingByType((s) => ({ ...s, [t]: false }));
        }
      })();
    });
  }, []);

  const loadSubtypes = async (type) => {
    // Return the list so callers get the loaded data immediately (useful for preview)
    if (subtypesByType[type]) return subtypesByType[type];
    setLoadingByType((s) => ({ ...s, [type]: true }));
    try {
      const resp = await componentAPI.getSubtypes(type);
      const normalized = normalizeSubtypes(resp?.data?.subtypes || resp?.data || []);

      // fetch heuristics for each subtype
      const withHeuristics = await Promise.all(
        (normalized.length ? normalized : [{ id: 'DEFAULT', name: 'DEFAULT', label: 'Default', heuristics: null }]).map(async (st) => {
          try {
            const hResp = await componentAPI.getHeuristicsForSubtype(type, st.id);
            return { ...st, heuristics: hResp.data };
          } catch (e) {
            return st;
          }
        })
      );

      const final = withHeuristics.length ? withHeuristics : [{ id: 'DEFAULT', name: 'DEFAULT', label: 'Default', heuristics: null }];
      setSubtypesByType((s) => ({ ...s, [type]: final }));
      setSelectedByType((s) => ({ ...s, [type]: (s[type] ?? final[0].id) }));
      return final;
    } catch (err) {
      const fallback = [{ id: 'DEFAULT', name: 'DEFAULT', label: 'Default', heuristics: null }];
      setSubtypesByType((s) => ({ ...s, [type]: fallback }));
      setSelectedByType((s) => ({ ...s, [type]: 'DEFAULT' }));
      return fallback;
    } finally {
      setLoadingByType((s) => ({ ...s, [type]: false }));
    }
  };

  const handleHover = async (type) => {
    if (pinnedType && pinnedType !== type) return;
    setOpenDropdown(type);
    const list = await loadSubtypes(type);
    // preview first/selected subtype in detail panel (includes heuristics)
    const chosen = (list || []).find((s) => s.id === selectedByType[type]) || (list || [])[0] || null;
    if (onPreviewSubtype) onPreviewSubtype(type, chosen);
  };

  const handleLeave = (type) => {
    if (pinnedType === type) return;
    setOpenDropdown(null);
    // clear preview only if not pinned
    if (!pinnedType && onPreviewSubtype) onPreviewSubtype(null, null);
  };

  const togglePin = (type) => {
    // toggle pin and ensure the preview subtype (with heuristics) is passed when pinned
    setPinnedType((p) => (p === type ? null : type));
    if (pinnedType !== type) {
      (async () => {
        const list = await loadSubtypes(type);
        setOpenDropdown(type);
        const chosen = (list || []).find((s) => s.id === selectedByType[type]) || (list || [])[0] || null;
        if (onPreviewSubtype) onPreviewSubtype(type, chosen);
      })();
    } else {
      setOpenDropdown(null);
      if (onPreviewSubtype) onPreviewSubtype(null, null);
    }
  };

  const handleSelectSubtype = (type, subtypeId, e) => {
    e && e.stopPropagation();
    setSelectedByType((s) => ({ ...s, [type]: subtypeId }));
    setPinnedType(type);
    setOpenDropdown(type);
    const chosen = (subtypesByType[type] || []).find((s) => s.id === subtypeId) || null;
    if (onPreviewSubtype) onPreviewSubtype(type, chosen);
  };

  const onDragStart = (event, componentType) => {
    const sublist = subtypesByType[componentType] || [];
    const chosen = sublist.find((s) => s.id === selectedByType[componentType]) || sublist[0] || null;
    const chosenId = chosen ? (chosen.id || chosen.name) : null;
    const payload = JSON.stringify({ type: componentType, subtype: chosenId });
    event.dataTransfer.setData('application/reactflow', payload);
    event.dataTransfer.setData('text/plain', componentType);
    event.dataTransfer.effectAllowed = 'move';
  };

  return (
    <div className="component-palette">
      <div className="palette-header">
        <h3>Components</h3>
        <p className="palette-subtitle">Hover a component to preview subtypes — click a subtype to pin</p>
      </div>

      <div className="palette-items">
        {COMPONENT_TYPES.map((component) => {
          const { type, icon, label, color } = component;
          const subtypes = subtypesByType[type] || [];
          const loading = !!loadingByType[type];
          const selectedId = selectedByType[type];
          const isOpen = openDropdown === type || pinnedType === type;

          return (
            <div
              key={type}
              className={`palette-item ${isOpen ? 'open' : ''}`}
              draggable
              onDragStart={(e) => onDragStart(e, type)}
              style={{ borderLeftColor: color, zIndex: isOpen ? 50 : 1 }}
              onMouseEnter={() => handleHover(type)}
              onMouseLeave={() => handleLeave(type)}
              onClick={() => togglePin(type)}
            >
              <div className="palette-item-left">
                <span className="palette-item-icon">{icon}</span>
                <div className="palette-item-info">
                  <div className="palette-item-label">{label}</div>
                  <div className="palette-item-type">{type}</div>
                </div>
              </div>

              <div className="palette-item-right">
                <div className="subtype-selected-inline">
                  {subtypes.length ? (subtypes.find((s) => s.id === selectedId)?.label || subtypes[0].label) : 'Loading'}
                </div>

                {isOpen && (
                  <div
                    className="subtype-panel below"
                    onMouseDown={(e) => e.stopPropagation()}
                    ref={panelRef}
                  >
                    {loading ? (
                      <div className="subtype-loading">Loading…</div>
                    ) : (
                      <>
                        <ul className="subtype-list-panel">
                          {subtypes.map((st) => (
                            <li
                              key={st.id}
                              className={`subtype-panel-item ${selectedId === st.id ? 'selected' : ''}`}
                              onClick={(e) => handleSelectSubtype(type, st.id, e)}
                            >
                              <div className="sp-left">
                                <div className="sp-title">{st.label}</div>
                                {/* removed heuristics from dropdown items by design */}
                              </div>
                              <div className="sp-right" style={{ minWidth: 24 }}>
                                {selectedId === st.id && <FaCheck className="subtype-check" />}
                              </div>
                            </li>
                          ))}
                        </ul>
                      </>
                    )}
                  </div>
                )}
              </div>
            </div>
          );
        })}
      </div>

      <div className="palette-footer">
        <div className="palette-help">
          <strong>Quick Tips:</strong>
          <ul>
            <li>Hover a component to preview subtypes</li>
            <li>Click a subtype to select & pin details in the right panel</li>
            <li>Drag the component to the canvas — selected subtype is included</li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default ComponentPalette;

