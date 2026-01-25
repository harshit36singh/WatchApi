import React from 'react';

export default function MetricsTab({ endpoints }) {
  const sortedEndpoints = [...endpoints].sort((a, b) => 
    (b.metrics?.total || 0) - (a.metrics?.total || 0)
  );

  const thStyle = {
    padding: '10px',
    textAlign: 'left',
    fontSize: '11px',
    color: '#0a0',
    fontWeight: 'normal'
  };

  const tdStyle = {
    padding: '8px 10px',
    fontSize: '12px',
    color: '#0f0'
  };

  return (
    <div>
      <h3 style={{ marginTop: 0, fontSize: '14px', color: '#0f0' }}>
        ENDPOINT METRICS
      </h3>
      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr style={{ borderBottom: '1px solid #0f0' }}>
            <th style={thStyle}>METHOD</th>
            <th style={thStyle}>PATH</th>
            <th style={thStyle}>CONTROLLER</th>
            <th style={{ ...thStyle, textAlign: 'right' }}>TOTAL</th>
            <th style={{ ...thStyle, textAlign: 'right' }}>2XX</th>
            <th style={{ ...thStyle, textAlign: 'right' }}>4XX</th>
            <th style={{ ...thStyle, textAlign: 'right' }}>5XX</th>
            <th style={{ ...thStyle, textAlign: 'right' }}>AVG MS</th>
          </tr>
        </thead>
        <tbody>
          {sortedEndpoints.map((ep, idx) => (
            <tr key={idx} style={{ borderBottom: '1px solid #0a0a0a' }}>
              <td style={tdStyle}>{ep.method || 'ANY'}</td>
              <td style={tdStyle}>{ep.path}</td>
              <td style={tdStyle}>{ep.controller}</td>
              <td style={{ ...tdStyle, textAlign: 'right' }}>
                {ep.metrics?.total || 0}
              </td>
              <td style={{ ...tdStyle, textAlign: 'right' }}>
                {ep.metrics?.['2xx'] || 0}
              </td>
              <td style={{ ...tdStyle, textAlign: 'right' }}>
                {ep.metrics?.['4xx'] || 0}
              </td>
              <td style={{ ...tdStyle, textAlign: 'right' }}>
                {ep.metrics?.['5xx'] || 0}
              </td>
              <td style={{ ...tdStyle, textAlign: 'right' }}>
                {(ep.metrics?.avgResponseMs || 0).toFixed(2)}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}