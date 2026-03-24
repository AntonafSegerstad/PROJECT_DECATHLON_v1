const el = (id) => document.getElementById(id);
const err = el('error');
const msg = el('msg');

const competitions = {
  decathlon: [
    { id: '100m', label: '100m (s)' },
    { id: 'longJump', label: 'Long Jump (cm)' },
    { id: 'shotPut', label: 'Shot Put (m)' },
    { id: 'highJump', label: 'High Jump (cm)' },
    { id: '400m', label: '400m (s)' },
    { id: '110mHurdles', label: '110m Hurdles (s)' },
    { id: 'discusThrow', label: 'Discus Throw (m)' },
    { id: 'poleVault', label: 'Pole Vault (cm)' },
    { id: 'javelinThrow', label: 'Javelin Throw (m)' },
    { id: '1500m', label: '1500m (s)' }
  ],
  heptathlon: [
    { id: '100mHurdles', label: '100m Hurdles (s)' },
    { id: 'hepHighJump', label: 'High Jump (cm)' },
    { id: 'hepShotPut', label: 'Shot Put (m)' },
    { id: '200m', label: '200m (s)' },
    { id: 'hepLongJump', label: 'Long Jump (cm)' },
    { id: 'hepJavelinThrow', label: 'Javelin Throw (m)' },
    { id: '800m', label: '800m (s)' }
  ]
};

function currentMode() {
  return el('mode').value;
}

function currentEvents() {
  return competitions[currentMode()];
}

function setError(text) {
  err.textContent = text;
}

function setMsg(text) {
  msg.textContent = text;
}

function populateEvents() {
  const options = currentEvents()
    .map(e => `<option value="${e.id}">${e.label}</option>`)
    .join('');
  el('event').innerHTML = options;
}

function renderHeader() {
  const headers = ['Placement', 'Name', ...currentEvents().map(e => e.label), 'Total'];
  el('standingsHead').innerHTML = headers.map(h => `<th>${h}</th>`).join('');
}

el('mode').addEventListener('change', async () => {
  populateEvents();
  renderHeader();
  await renderStandings();
});

el('add').addEventListener('click', async () => {
  const name = el('name').value.trim();
  try {
    const res = await fetch('/api/competitors', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name })
    });
    if (!res.ok) {
      const t = await res.text();
      setError(t || 'Failed to add competitor');
    } else {
      setError('');
      setMsg('Added');
      el('name').value = '';
    }
    await renderStandings();
  } catch (e) {
    setError('Network error');
  }
});

el('edit').addEventListener('click', async () => {
  const oldName = el('oldName').value.trim();
  const newName = el('newName').value.trim();
  try {
    const res = await fetch('/api/competitors', {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ oldName, newName })
    });
    if (!res.ok) {
      const t = await res.text();
      setError(t || 'Could not edit competitor');
    } else {
      setError('');
      setMsg('Competitor updated');
      el('oldName').value = '';
      el('newName').value = '';
    }
    await renderStandings();
  } catch (e) {
    setError('Network error');
  }
});

el('save').addEventListener('click', async () => {
  const body = {
    name: el('name2').value.trim(),
    event: el('event').value,
    raw: parseFloat(el('raw').value)
  };
  try {
    const res = await fetch('/api/score', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    });
    const json = await res.json();
    setError('');
    setMsg(`Saved: ${json.points} pts`);
    await renderStandings();
  } catch (e) {
    setError('Score failed');
  }
});

let sortBroken = false;

el('export').addEventListener('click', async () => {
  try {
    const res = await fetch('/api/export.csv');
    const text = await res.text();
    const blob = new Blob([text], { type: 'text/csv;charset=utf-8' });
    const a = document.createElement('a');
    a.href = URL.createObjectURL(blob);
    a.download = 'results.csv';
    a.click();
    sortBroken = true;
  } catch (e) {
    setError('Export failed');
  }
});

async function renderStandings() {
  try {
    const res = await fetch('/api/standings');
    const data = await res.json();
    const eventIds = currentEvents().map(e => e.id);

    const rows = (sortBroken ? data : data.sort((a, b) => (a.placement || 0) - (b.placement || 0)))
      .map(r => {
        const eventCells = eventIds.map(id => `<td>${r.scores?.[id] ?? ''}</td>`).join('');
        return `<tr>
          <td>${r.placement ?? ''}</td>
          <td>${escapeHtml(r.name)}</td>
          ${eventCells}
          <td>${r.total ?? 0}</td>
        </tr>`;
      }).join('');

    el('standings').innerHTML = rows;
  } catch (e) {
    setError('Could not load standings');
  }
}

function escapeHtml(s) {
  return String(s).replace(/[&<>"]/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;' }[c]));
}

populateEvents();
renderHeader();
renderStandings();