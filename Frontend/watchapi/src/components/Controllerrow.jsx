import React from 'react';
import EndpointTable from './EndpointTable';

export default function ControllerRow({ name, stats, isExpanded, onToggle }) {
  const tdStyle = {
    padding: '8px 10px',
    fontSize: '12px',
    color: '#0f0'
  };

  return (
    <div style={{ borderBottom: '1px solid #0a0a0a' }}>
     
      <div
        onClick={onToggle}
        style={{
          display: 'grid',
          gridTemplateColumns: '30px 1fr auto auto auto',
          gap: '10px',
          padding: '12px',
          cursor: 'pointer',
          backgroundColor: isExpanded ? '#0f0a00' : 'transparent',
          transition: 'background-color 0.2s'
        }}
      >
        <div style={{ color: '#0f0', fontSize: '12px' }}>
          {isExpanded ? '▼' : '▶'}
        </div>
        <div style={{ ...tdStyle, fontWeight: 'bold' }}>
          {name}
        </div>
        <div style={{ ...tdStyle, textAlign: 'right', minWidth: '80px' }}>
          {stats.count} endpoint{stats.count !== 1 ? 's' : ''}
        </div>
        <div style={{ ...tdStyle, textAlign: 'right', minWidth: '120px' }}>
          {stats.total.toLocaleString()} requests
        </div>
        <div style={{ ...tdStyle, textAlign: 'right', minWidth: '100px' }}>
          {stats.avg.toFixed(2)}ms avg
        </div>
      </div>

      {/* Expanded Endpoints Table */}
      {isExpanded && (
        <div style={{ backgroundColor: '#050505', padding: '10px 10px 10px 40px' }}>
          <EndpointTable endpoints={stats.endpoints} />
        </div>
      )}
    </div>
  );
}