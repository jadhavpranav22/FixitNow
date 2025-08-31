import React, { useEffect, useState } from "react";
import api from "../api/axiosConfig";
import { useAuth } from "../contexts/AuthContext";
import { useNotifications } from "../contexts/NotificationContext";

const ContractorDashboard = () => {
  const { token, userId } = useAuth();
  const { addNotification } = useNotifications();
  const [pending, setPending] = useState([]);
  const [accepted, setAccepted] = useState([]);
  const [completed, setCompleted] = useState([]);
  const [loading, setLoading] = useState(true);
  const [actioningId, setActioningId] = useState(null);

  const fetchBookings = async () => {
    try {
      const pendingRes = await api.get(`/bookings/contractor/${userId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      const acceptedRes = await api.get(
        `/bookings/contractor/${userId}/accepted`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      const completedRes = await api.get(
        `/bookings/contractor/${userId}/completed`,
        { headers: { Authorization: `Bearer ${token}` } }
      );

      const rawPending = Array.isArray(pendingRes.data) ? pendingRes.data : [];
      // Only show actionable bookings in the Pending section
      const actionablePending = rawPending.filter(
        (b) => b.status === "PENDING" || b.status === "ASSIGNED_TO_CONTRACTOR"
      );
      setPending(actionablePending);
      setAccepted(Array.isArray(acceptedRes.data) ? acceptedRes.data : []);
      setCompleted(Array.isArray(completedRes.data) ? completedRes.data : []);
    } catch (err) {
      console.error("Error fetching bookings:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBookings();
  }, []);

  const getStatusBadgeClass = (status) => {
    switch (status) {
      case "ACCEPTED_BY_CONTRACTOR":
        return "bg-success";
      case "REJECTED_BY_CONTRACTOR":
        return "bg-danger";
      case "ASSIGNED_TO_CONTRACTOR":
        return "bg-primary";
      default:
        return "bg-warning text-dark"; // PENDING and others
    }
  };

  const getStatusText = (status) => {
    switch (status) {
      case "PENDING":
        return "Pending";
      case "ACCEPTED_BY_CONTRACTOR":
        return "Accepted by Contractor";
      case "REJECTED_BY_CONTRACTOR":
        return "Rejected by Contractor";
      case "ASSIGNED_TO_CONTRACTOR":
        return "Assigned to You";
      case "COMPLETED":
        return "Completed";
      default:
        return status;
    }
  };

  const handleAccept = async (booking) => {
    try {
      console.log('Contractor: Accepting booking', booking.id, 'for contractor', userId);
      setActioningId(booking.id);
      const res = await api.put(`/bookings/${booking.id}/accept`, null, {
        params: { contractorId: userId },
      });
      console.log('Contractor: Accept response:', res.data);
      
      // Remove from pending and refresh lists
      setPending((prev) => prev.filter((b) => b.id !== booking.id));
      // If API returns updated booking, prepend to accepted list
      if (res?.data) {
        setAccepted((prev) => [res.data, ...prev]);
      } else {
        await fetchBookings();
      }
      
      // Add notification for contractor
      addNotification({
        message: `✅ Successfully accepted booking for ${booking.address || 'the service'}! The customer has been notified.`,
        severity: 'success'
      });
      
    } catch (err) {
      console.error("Error accepting booking:", err);
      addNotification({
        message: `❌ Failed to accept booking: ${err.response?.data || err.message}`,
        severity: 'error'
      });
    } finally {
      setActioningId(null);
    }
  };

  const handleReject = async (booking) => {
    try {
      setActioningId(booking.id);
      await api.put(`/bookings/${booking.id}/reject`);
      setPending((prev) => prev.filter((b) => b.id !== booking.id));
    } catch (err) {
      console.error("Error rejecting booking:", err);
    } finally {
      setActioningId(null);
    }
  };

  const renderBookings = (list, showActions = false) => {
    if (!list || list.length === 0) {
      return <p className="text-muted fst-italic">No bookings available.</p>;
    }

    return (
      <div className="row g-4">
        {list.map((b) => (
          <div key={b.id} className="col-12 col-sm-6 col-lg-4">
            <div className="card h-100 shadow-sm border-0">
              {b.images ? (
                <img
                  src={b.images.split(",")[0]}
                  className="card-img-top"
                  alt="Booking"
                  style={{ height: "200px", objectFit: "cover" }}
                />
              ) : (
                <div
                  className="bg-light d-flex align-items-center justify-content-center"
                  style={{ height: "200px" }}
                >
                  <span className="text-muted">No Image</span>
                </div>
              )}
              <div className="card-body d-flex flex-column">
                <h5 className="card-title text-truncate">{b.address}</h5>
                <p className="card-text text-muted small mb-2">
                  {b.notes || "No notes provided"}
                </p>

                <span className={`badge mb-3 ${getStatusBadgeClass(b.status)}`}>
                  {getStatusText(b.status)}
                </span>

                {showActions && (
                  <div className="mt-auto d-flex gap-2">
                    <button
                      className="btn btn-sm btn-success w-50"
                      onClick={() => handleAccept(b)}
                      disabled={actioningId === b.id}
                    >
                      {actioningId === b.id ? "Accepting..." : "Accept"}
                    </button>
                    <button
                      className="btn btn-sm btn-danger w-50"
                      onClick={() => handleReject(b)}
                      disabled={actioningId === b.id}
                    >
                      {actioningId === b.id ? "Rejecting..." : "Reject"}
                    </button>
                  </div>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>
    );
  };

  if (loading) {
    return (
      <div className="p-4 text-center text-secondary">Loading...</div>
    );
  }

  return (
    <div className="container py-5">
      <section>
        <h2 className="mb-4 border-bottom pb-2">Pending Bookings</h2>
        {renderBookings(pending, true)}
      </section>

      <section className="mt-5">
        <h2 className="mb-4 border-bottom pb-2">Accepted Bookings</h2>
        {renderBookings(accepted)}
      </section>

      <section className="mt-5">
        <h2 className="mb-4 border-bottom pb-2">Completed Bookings</h2>
        {renderBookings(completed)}
      </section>
    </div>
  );
};

export default ContractorDashboard;
