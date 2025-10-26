import React from 'react';
import { Handle, Position } from 'reactflow';
import './ComponentNode.css';

const COMPONENT_ICONS = {
  DATABASE: '💾',
  CACHE: '⚡',
  API_SERVICE: '🔌',
  QUEUE: '📬',
  STORAGE: '📦',
  LOAD_BALANCER: '⚖️',
  STREAM_PROCESSOR: '🌊',
  BATCH_PROCESSOR: '⏱️',
  EXTERNAL_SERVICE: '🌐',
  CLIENT: '👤',
};

const COMPONENT_COLORS = {
  DATABASE: '#10b981',
  CACHE: '#f59e0b',
  API_SERVICE: '#3b82f6',
  QUEUE: '#8b5cf6',
  STORAGE: '#ec4899',
  LOAD_BALANCER: '#06b6d4',
  STREAM_PROCESSOR: '#6366f1',
  BATCH_PROCESSOR: '#84cc16',
  EXTERNAL_SERVICE: '#64748b',
  CLIENT: '#0ea5e9',
};

const ComponentNode = ({ data, isConnectable }) => {
  const icon = COMPONENT_ICONS[data.componentType] || '📦';
  const color = COMPONENT_COLORS[data.componentType] || '#6b7280';

  // Format the display label with subtype if available
  const getDisplayLabel = () => {
    if (data.subtype) {
      return (
        <>
          <div className="node-subtype">{data.subtype.replace(/_/g, ' ')}</div>
          <div className="node-custom-name">{data.customName || data.label}</div>
        </>
      );
    }
    return <div className="node-custom-name">{data.customName || data.label}</div>;
  };

  return (
    <div className="component-node" style={{ borderColor: color }}>
      <Handle
        type="target"
        position={Position.Top}
        isConnectable={isConnectable}
        className="node-handle"
      />
      <div className="node-content">
        <div className="node-icon" style={{ backgroundColor: color }}>
          {icon}
        </div>
        <div className="node-label">{getDisplayLabel()}</div>
      </div>
      <Handle
        type="source"
        position={Position.Bottom}
        isConnectable={isConnectable}
        className="node-handle"
      />
    </div>
  );
};

export default ComponentNode;
