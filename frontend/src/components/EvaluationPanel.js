import React from 'react';
import './EvaluationPanel.css';

const EvaluationPanel = ({ evaluation, onClose }) => {
  if (!evaluation) return null;

  const renderHeuristicScores = (scores) => {
    if (!scores || Object.keys(scores).length === 0) return null;

    return (
      <div className="scores-grid">
        {Object.entries(scores).map(([key, value]) => (
          <div key={key} className="score-card">
            <div className="score-label">{key.replace('_', ' ')}</div>
            <div className="score-bar">
              <div
                className="score-fill"
                style={{
                  width: `${(value / 10) * 100}%`,
                  backgroundColor: value >= 7 ? '#10b981' : value >= 4 ? '#f59e0b' : '#ef4444'
                }}
              />
            </div>
            <div className="score-value">{value.toFixed(2)}/10</div>
          </div>
        ))}
      </div>
    );
  };

  const renderValidation = (validation) => {
    if (!validation) return null;

    return (
      <div className="validation-section">
        <h3>Validation Results</h3>
        {validation.valid ? (
          <div className="validation-success">
            <span className="status-icon">✅</span>
            <span>Architecture is valid!</span>
          </div>
        ) : (
          <div className="validation-errors">
            <div className="validation-header">
              <span className="status-icon">⚠️</span>
              <span>{validation.violations?.length || 0} violations found</span>
            </div>
            {validation.violations && validation.violations.length > 0 && (
              <ul className="violations-list">
                {validation.violations.map((violation, index) => (
                  <li key={index}>{violation}</li>
                ))}
              </ul>
            )}
          </div>
        )}
      </div>
    );
  };

  return (
    <div className="evaluation-overlay">
      <div className="evaluation-panel">
        <div className="evaluation-header">
          <h2>📊 Architecture Evaluation</h2>
          <button className="close-btn" onClick={onClose}>×</button>
        </div>

        <div className="evaluation-content">
          {/* Overall Score */}
          {evaluation.overallScore !== undefined && (
            <div className="overall-score-section">
              <h3>Overall Score</h3>
              <div className="overall-score">
                <div className="score-circle">
                  <svg width="120" height="120">
                    <circle
                      cx="60"
                      cy="60"
                      r="50"
                      fill="none"
                      stroke="#e5e7eb"
                      strokeWidth="10"
                    />
                    <circle
                      cx="60"
                      cy="60"
                      r="50"
                      fill="none"
                      stroke="#3b82f6"
                      strokeWidth="10"
                      strokeDasharray={`${(evaluation.overallScore / 10) * 314} 314`}
                      strokeDashoffset="0"
                      transform="rotate(-90 60 60)"
                    />
                  </svg>
                  <div className="score-text">
                    {evaluation.overallScore.toFixed(2)}
                    <span>/10</span>
                  </div>
                </div>
                <div className="score-label-main">
                  {evaluation.overallScore >= 7 ? 'Excellent' :
                   evaluation.overallScore >= 5 ? 'Good' :
                   evaluation.overallScore >= 3 ? 'Fair' : 'Poor'}
                </div>
              </div>
            </div>
          )}

          {/* Component Scores */}
          {evaluation.componentScores && Object.keys(evaluation.componentScores).length > 0 && (
            <div className="section">
              <h3>Component Scores</h3>
              {renderHeuristicScores(evaluation.componentScores)}
            </div>
          )}

          {/* Link Scores */}
          {evaluation.linkScores && Object.keys(evaluation.linkScores).length > 0 && (
            <div className="section">
              <h3>Link Scores</h3>
              {renderHeuristicScores(evaluation.linkScores)}
            </div>
          )}

          {/* Architecture Metrics */}
          {evaluation.architectureMetrics && (
            <div className="section">
              <h3>Architecture Metrics</h3>
              <div className="metrics-grid">
                <div className="metric-card">
                  <div className="metric-label">Total Components</div>
                  <div className="metric-value">{evaluation.architectureMetrics.totalComponents || 0}</div>
                </div>
                <div className="metric-card">
                  <div className="metric-label">Total Links</div>
                  <div className="metric-value">{evaluation.architectureMetrics.totalLinks || 0}</div>
                </div>
                <div className="metric-card">
                  <div className="metric-label">Avg Component Score</div>
                  <div className="metric-value">
                    {evaluation.architectureMetrics.avgComponentScore?.toFixed(2) || 'N/A'}
                  </div>
                </div>
                <div className="metric-card">
                  <div className="metric-label">Avg Link Score</div>
                  <div className="metric-value">
                    {evaluation.architectureMetrics.avgLinkScore?.toFixed(2) || 'N/A'}
                  </div>
                </div>
              </div>
            </div>
          )}

          {/* Validation */}
          {evaluation.validation && renderValidation(evaluation.validation)}

          {/* Recommendations */}
          {evaluation.recommendations && evaluation.recommendations.length > 0 && (
            <div className="section">
              <h3>💡 Recommendations</h3>
              <ul className="recommendations-list">
                {evaluation.recommendations.map((rec, index) => (
                  <li key={index}>{rec}</li>
                ))}
              </ul>
            </div>
          )}
        </div>

        <div className="evaluation-footer">
          <button className="btn btn-primary" onClick={onClose}>
            Close
          </button>
        </div>
      </div>
    </div>
  );
};

export default EvaluationPanel;

