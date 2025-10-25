import React from 'react';
import './Sidebar.css';

const Sidebar = ({ selectedNode, selectedEdge, onDeleteNode, onDeleteEdge }) => {
  if (!selectedNode && !selectedEdge) {
    return (
      <div className="sidebar">
        <div className="sidebar-empty">
          <p>👈 Select a component or connection to view details</p>
        </div>
      </div>
    );
  }

  if (selectedNode) {
    return (
      <div className="sidebar">
        <div className="sidebar-header">
          <h3>Component Details</h3>
        </div>
        <div className="sidebar-content">
          <div className="detail-section">
            <label>Name:</label>
            <div className="detail-value">{selectedNode.data.label}</div>
          </div>
          <div className="detail-section">
            <label>Type:</label>
            <div className="detail-value badge">{selectedNode.data.componentType}</div>
          </div>
          <div className="detail-section">
            <label>ID:</label>
            <div className="detail-value code">{selectedNode.data.componentId}</div>
          </div>

          {selectedNode.data.heuristics && (
            <div className="detail-section">
              <label>Heuristics:</label>
              <div className="heuristics-grid">
                {Object.entries(selectedNode.data.heuristics.scores || {}).map(([key, value]) => (
                  <div key={key} className="heuristic-item">
                    <span className="heuristic-name">{key.replace('_', ' ')}</span>
                    <div className="heuristic-bar">
                      <div
                        className="heuristic-fill"
                        style={{ width: `${(value / 10) * 100}%` }}
                      />
                    </div>
                    <span className="heuristic-value">{value.toFixed(1)}</span>
                  </div>
                ))}
              </div>
            </div>
          )}

          {selectedNode.data.properties && Object.keys(selectedNode.data.properties).length > 0 && (
            <div className="detail-section">
              <label>Properties:</label>
              <div className="properties-list">
                {Object.entries(selectedNode.data.properties).map(([key, value]) => (
                  <div key={key} className="property-item">
                    <span className="property-key">{key}:</span>
                    <span className="property-value">{JSON.stringify(value)}</span>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
        <div className="sidebar-actions">
          <button
            className="btn btn-danger btn-block"
            onClick={() => onDeleteNode(selectedNode.id)}
          >
            🗑️ Delete Component
          </button>
        </div>
      </div>
    );
  }

  if (selectedEdge) {
    return (
      <div className="sidebar">
        <div className="sidebar-header">
          <h3>Connection Details</h3>
        </div>
        <div className="sidebar-content">
          <div className="detail-section">
            <label>Link Type:</label>
            <div className="detail-value badge">{selectedEdge.data?.linkType || 'Unknown'}</div>
          </div>
          <div className="detail-section">
            <label>ID:</label>
            <div className="detail-value code">{selectedEdge.data?.linkId || selectedEdge.id}</div>
          </div>

          {selectedEdge.data?.heuristics && (
            <div className="detail-section">
              <label>Link Heuristics:</label>
              <div className="heuristics-grid">
                {Object.entries(selectedEdge.data.heuristics.scores || {}).map(([key, value]) => (
                  <div key={key} className="heuristic-item">
                    <span className="heuristic-name">{key.replace('_', ' ')}</span>
                    <div className="heuristic-bar">
                      <div
                        className="heuristic-fill"
                        style={{ width: `${(value / 10) * 100}%` }}
                      />
                    </div>
                    <span className="heuristic-value">{value.toFixed(1)}</span>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
        <div className="sidebar-actions">
          <button
            className="btn btn-danger btn-block"
            onClick={() => onDeleteEdge(selectedEdge.id)}
          >
            🗑️ Delete Connection
          </button>
        </div>
      </div>
    );
  }

  return null;
};

export default Sidebar;

