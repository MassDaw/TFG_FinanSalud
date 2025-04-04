// Componente para la tabla de archivos
function FileTable({ files }) {
    return (
        <div className="bg-white rounded-lg shadow overflow-hidden">
            <table className="min-w-full">
                <thead className="bg-gray-50">
                <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Fecha
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Nombre del archivo
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Subido por
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Tamaño
                    </th>
                </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                {files.map((file, index) => (
                    <tr key={index} className="hover:bg-gray-50">
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                            {file.fecha}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-blue-500">
                            {file.nombre}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                            {file.autor}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                            {file.tamano}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}