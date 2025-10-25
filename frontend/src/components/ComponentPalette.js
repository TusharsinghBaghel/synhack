import React from 'react';
import './ComponentPalette.css';

const COMPONENT_TYPES = [
  { type: 'DATABASE', icon: '💾', label: 'Database', color: '#10b981' },
  { type: 'CACHE', icon: '⚡', label: 'Cache', color: '#f59e0b' },
  { type: 'API_SERVICE', icon: '🔌', label: 'API Service', color: '#3b82f6' },
  { type: 'QUEUE', icon: '📬', label: 'Queue', color: '#8b5cf6' },
  { type: 'STORAGE', icon: '📦', label: 'Storage', color: '#ec4899' },
  { type: 'LOAD_BALANCER', icon: '⚖️', label: 'Load Balancer', color: '#06b6d4' },
  { type: 'STREAM_PROCESSOR', icon: '🌊', label: 'Stream Processor', color: '#6366f1' },
  { type: 'BATCH_PROCESSOR', icon: '⏱️', label: 'Batch Processor', color: '#84cc16' },
  { type: 'EXTERNAL_SERVICE', icon: '🌐', label: 'External Service', color: '#64748b' },
  { type: 'CLIENT', icon: '👤', label: 'Client', color: '#0ea5e9' },
];

const ComponentPalette = () => {
  const onDragStart = (event, componentType) => {
    event.dataTransfer.setData('application/reactflow', componentType);
    event.dataTransfer.effectAllowed = 'move';
  };

  return (
    <div className="component-palette">
      <div className="palette-header">
        <h3>Components</h3>
        <p className="palette-subtitle">Drag & drop to add</p>
      </div>
      <div className="palette-items">
        {COMPONENT_TYPES.map((component) => (
          <div
            key={component.type}
            className="palette-item"
            draggable
            onDragStart={(e) => onDragStart(e, component.type)}
            style={{ borderLeftColor: component.color }}
          >
            <span className="palette-item-icon">{component.icon}</span>
            <div className="palette-item-info">
              <div className="palette-item-label">{component.label}</div>
              <div className="palette-item-type">{component.type}</div>
            </div>
          </div>
        ))}
      </div>
      <div className="palette-footer">
        <div className="palette-help">
          <strong>💡 Quick Tips:</strong>
          <ul>
            <li>Drag components to canvas</li>
            <li>Connect by dragging from handles</li>
            <li>Click to view properties</li>
            <li>Connections are validated automatically</li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default ComponentPalette;

