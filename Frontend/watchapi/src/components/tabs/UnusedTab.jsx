import React from 'react';

export default function UnusedTab({ unused }) {
  const thStyle = {
    padding: '10px',
    textAlign: 'left',
    fontSize: '11px',
    fontWeight: 'normal'
  };

  const tdStyle = {
    padding: '8px 10px',
    fontSize: '12px'
  };

  return (
    <div>
      <h3 style={{ marginTop: 0, fontSize: '14px', color: '#f00' }}>
        UNUSED ENDPOINTS
      </h3>
      {unused.length === 0 ? (
        <p style={{ color: '#0a0' }}>No unused endpoints detected</p>
      ) : (
        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr style={{ borderBottom: '1px solid #f00' }}>
              <th style={{ ...thStyle, color: '#f00' }}>METHOD</th>
              <th style={{ ...thStyle, color: '#f00' }}>PATH</th>
              <th style={{ ...thStyle, color: '#f00' }}>CONTROLLER</th>
            </tr>
          </thead>
          <tbody>
            {unused.map((ep, idx) => (
              <tr key={idx} style={{ borderBottom: '1px solid #0a0a0a' }}>
                <td style={{ ...tdStyle, color: '#f00' }}>
                  {ep.method || 'ANY'}
                </td>
                <td style={{ ...tdStyle, color: '#f00' }}>
                  {ep.path}
                </td>
                <td style={{ ...tdStyle, color: '#f00' }}>
                  {ep.controller}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}