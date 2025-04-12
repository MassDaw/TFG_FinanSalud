const dropZone = document.getElementById("drop-zone");
const fileInput = document.getElementById("file-input");
const tableBody = document.getElementById("file-table-body");

document.getElementById("scan-btn").addEventListener("click", () => {
    fileInput.click();
});

fileInput.addEventListener("change", handleFiles);

dropZone.addEventListener("dragover", (e) => {
    e.preventDefault();
    dropZone.classList.add("dragging");
});

dropZone.addEventListener("dragleave", () => {
    dropZone.classList.remove("dragging");
});

dropZone.addEventListener("drop", (e) => {
    e.preventDefault();
    dropZone.classList.remove("dragging");
    const files = e.dataTransfer.files;
    handleFiles({ target: { files } });
});

function handleFiles(e) {
    const files = e.target.files;
    for (let file of files) {
        const row = document.createElement("tr");
        const now = new Date();
        row.innerHTML = `
      <td>${now.toLocaleDateString("es-ES", { day: '2-digit', month: 'short' })}</td>
      <td><a href="#">${file.name}</a></td>
      <td>@Tú</td>
      <td>${(file.size / 1024).toFixed(0)}KB</td>
    `;
        tableBody.appendChild(row);

        // Aquí puedes hacer la petición al backend:
        // enviarArchivo(file);
    }
}
